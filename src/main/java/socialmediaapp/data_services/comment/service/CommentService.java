package socialmediaapp.data_services.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import socialmediaapp.data_services.comment.model.Comment;
import socialmediaapp.data_services.comment.model.CommentDTO;
import socialmediaapp.data_services.comment.repository.CommentRepository;
import socialmediaapp.data_services.post.model.Post;
import socialmediaapp.data_services.post.repository.PostRepository;
import socialmediaapp.data_services.user.model.User;
import socialmediaapp.data_services.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentDTOMapper commentDTOMapper;

    //GET
    public List<CommentDTO> findCommentsByPosts(Post post) {
        return commentRepository
                .findAllByPost(post)
                .stream()
                .map(commentDTOMapper)
                .toList();
    }

    //POST
    public CommentDTO createComment(UserDetails userDetails, Comment comment) {
        User user = getUser(userDetails);
        Post post = getPost(comment.getPost().getUuid());

        comment.setUser(user);
        comment.setPost(post);

        return commentDTOMapper.apply(commentRepository.save(comment));
    }


    private Post getPost(String uuid) {
        return postRepository.findById(uuid)
                .orElseThrow();
    }
    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();
    }
}
