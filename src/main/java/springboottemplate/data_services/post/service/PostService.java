package springboottemplate.data_services.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.*;
import springboottemplate.data_services.friendship.Friendship;
import springboottemplate.data_services.friendship.FriendshipRepository;
import springboottemplate.data_services.friendship.FriendshipStatus;
import springboottemplate.data_services.post.Post;
import springboottemplate.data_services.post.PostDTO;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.repository.UserRepository;
import springboottemplate.utilities.EmailValidationService;

import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor

@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostDTOMapper postDTOMapper;
    private final FriendshipRepository friendshipService;
    private final EmailValidationService emailValidationService;

    //GET
    public List<PostDTO> findAllPostsByUsersFriend(UserDetails userDetails) throws UserNotFoundException {

        User user = getUser(userDetails);

        List<User> friends = friendshipService.
                getFriendshipByUserAndFriendshipStatusEquals(user, FriendshipStatus.ACCEPTED)
                .stream()
                .map(Friendship::getFriend)
                .toList();

        return postRepository.findAllByUserIn(friends)
                .stream()
                .map(postDTOMapper)
                .toList();
    }


    public List<PostDTO> findAllPostsByUser(UserDetails userDetails) throws UserNotFoundException {

        User user = getUser(userDetails);

        return postRepository.findAllByUser(user)
                .stream()
                .map(postDTOMapper)
                .toList();

    }

    //POST
    public PostDTO save(UserDetails userDetails, Post post) throws UserNotFoundException, EntityShouldNotContainAnId {

        if (post.getId() != null) throw new EntityShouldNotContainAnId(Post.class);

        User user = getUser(userDetails);

        post.setUser(user);
        Post savedPost = postRepository.save(post);

        return postDTOMapper.apply(savedPost);
    }

    //UPDATE
    public PostDTO updatePost(UserDetails userDetails, Post post) throws
            EntityShouldContainAnId,
            UserNotFoundException,
            EntityDoesNotExistException,
            UserDoesNotOwnEntityException {

        if (post.getId() == null) throw new EntityShouldContainAnId(Post.class);

        User user = getUser(userDetails);

        Post postFromDb = postRepository.findById(post.getId())
                .orElseThrow(() -> new EntityDoesNotExistException(Post.class));

        if (!Objects.equals(postFromDb.getUser().getUserId(), user.getUserId()))
            throw new UserDoesNotOwnEntityException(user, Post.class);

        post.setUser(user);
        Post updatedPost = postRepository.save(post);

        return postDTOMapper.apply(updatedPost);
    }

    //DELETE
    public void deletePost(UserDetails userDetails, Integer id) throws EntityDoesNotExistException, UserNotFoundException {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException(Post.class));

        User user = getUser(userDetails);

        if (Objects.equals(post.getUser().getUserId(), user.getUserId())) {
            postRepository.deleteById(post.getId());
        }
    }

    private User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }
}
