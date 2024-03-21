package springboottemplate.data_services.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.EntityDoesNotExistException;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.EntityShouldNotContainAnId;
import springboottemplate.data_services.exception.UserDoesNotOwnEntityException;
import springboottemplate.data_services.post.exception.InvalidPostException;
import springboottemplate.data_services.post.model.Post;
import springboottemplate.data_services.post.model.PostDTO;
import springboottemplate.data_services.post.model.PostReaction;
import springboottemplate.data_services.post.model.PostReactionType;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.user.exceptions.UserNotFoundException;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor

@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostDTOMapper postDTOMapper;

    public List<PostDTO> findAllPostsByUserAndHisFriends(UserDetails userDetails, Pageable pageable) throws UserNotFoundException {
        User user = getUser(userDetails);
        List<Post> posts = postRepository.findAllPostsByUserAndHisFriends(user, pageable);

        return posts.stream()
                .map(postDTOMapper)
                .toList();
    }

    public List<PostReaction> addReaction(UserDetails userDetails, String uuid, PostReactionType postReactionType) throws UserNotFoundException {
        User user = getUser(userDetails);
        Post post = postRepository.findById(uuid).orElseThrow();

        if (post.getPostReactions() == null) {
            post.setPostReactions(new ArrayList<>());
        }

        Optional<PostReaction> currentReactionOfUserToThisPost = post.getPostReactions()
                .stream()
                .filter(postReaction -> user.getUuid().equals(postReaction.getUser().getUuid()))
                .findFirst();

        if (currentReactionOfUserToThisPost.isEmpty()) {
            PostReaction postReaction = PostReaction
                    .builder()
                    .user(user)
                    .reaction(postReactionType)
                    .build();
            post.getPostReactions().add(postReaction);
            Post savedPost = postRepository.save(post);
            return savedPost.getPostReactions();
        } else {
            PostReaction currentReaction = currentReactionOfUserToThisPost.get();
            if (currentReaction.getReaction().equals(postReactionType)) {
                post.getPostReactions().removeIf(postReaction -> user.getUuid().equals(postReaction.getUser().getUuid()));
                Post savedPost = postRepository.save(post);
                return savedPost.getPostReactions();
            } else {
                post.getPostReactions().removeIf(postReaction -> user.getUuid().equals(postReaction.getUser().getUuid()));
                PostReaction postReaction = PostReaction
                        .builder()
                        .user(user)
                        .reaction(postReactionType)
                        .build();
                post.getPostReactions().add(postReaction);
                Post savedPost = postRepository.save(post);
                return savedPost.getPostReactions();
            }
        }
    }

    public PostDTO save(UserDetails userDetails, Post post) throws UserNotFoundException, EntityShouldNotContainAnId, InvalidPostException {
        if (post.getUuid() != null) throw new EntityShouldNotContainAnId(Post.class);

//        boolean isPostValid = (post.getContent() != null && !post.getContent().isEmpty()) ||
//                (post.getImages() != null && !post.getImages().isEmpty());
//        if (!isPostValid)
//            throw new InvalidPostException();

        User user = getUser(userDetails);

        post.setUser(user);
        Post savedPost = postRepository.save(post);

        return postDTOMapper.apply(savedPost);
    }

    public PostDTO updatePost(UserDetails userDetails, Post post) throws
            EntityShouldContainAnId,
            UserNotFoundException,
            EntityDoesNotExistException,
            UserDoesNotOwnEntityException {

        if (post.getUuid() == null) throw new EntityShouldContainAnId(Post.class);

        User user = getUser(userDetails);

        Post postFromDb = postRepository.findById(post.getUuid())
                .orElseThrow(() -> new EntityDoesNotExistException(Post.class));

        if (!Objects.equals(postFromDb.getUser().getUuid(), user.getUuid()))
            throw new UserDoesNotOwnEntityException(user, Post.class);

        post.setUser(user);
        Post updatedPost = postRepository.save(post);

        return postDTOMapper.apply(updatedPost);
    }

    public void deletePost(UserDetails userDetails, String id)
            throws EntityDoesNotExistException,
            UserNotFoundException,
            UserDoesNotOwnEntityException {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException(Post.class));
        User user = getUser(userDetails);

        if (!Objects.equals(post.getUser().getUuid(), user.getUuid())) {
            throw new UserDoesNotOwnEntityException(user, Post.class);
        }

        postRepository.deleteById(post.getUuid());
    }

    public List<PostDTO> findAllPostsByUserId(String uuid, Pageable pageable) {
        User user = userRepository.findById(uuid)
                .orElseThrow();

        return postRepository.findAllByUserOrderByCreatedAt(user, pageable)
                .stream()
                .map(postDTOMapper)
                .toList();
    }

    private User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }

}