package springboottemplate.data_services.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.comment.model.Comment;
import springboottemplate.data_services.comment.model.CommentDTO;
import springboottemplate.data_services.comment.service.CommentService;
import springboottemplate.data_services.post.model.Post;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/by-post")
    public List<CommentDTO> findCommentsByPosts(@RequestBody Post post) {
        return commentService.findCommentsByPosts(post);
    }

    @PostMapping("/add")
    public ResponseEntity<CommentDTO> addComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Comment comment) {
        System.out.println("Hello");
        try {
            CommentDTO addedComment = commentService.createComment(userDetails, comment);
            return new ResponseEntity<>(addedComment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
