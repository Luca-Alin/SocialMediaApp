package springboottemplate.data_services.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.comment.Comment;
import springboottemplate.data_services.comment.CommentDTO;
import springboottemplate.data_services.comment.repository.CommentRepository;
import springboottemplate.data_services.post.Post;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.repository.UserRepository;

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
        Post post = getPost(comment.getPost().getId());

        comment.setUser(user);
        comment.setPost(post);

        return commentDTOMapper.apply(commentRepository.save(comment));
    }


    private Post getPost(Integer id) {
        return postRepository.findById(id)
                .orElseThrow();
    }
    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();
    }
}
