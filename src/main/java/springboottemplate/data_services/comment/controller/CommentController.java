package springboottemplate.data_services.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboottemplate.data_services.comment.CommentDTO;
import springboottemplate.data_services.comment.service.CommentService;
import springboottemplate.data_services.post.Post;

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
}
