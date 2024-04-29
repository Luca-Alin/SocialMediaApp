package springboottemplate.data_services.comment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import springboottemplate.data_services.comment.model.Comment;
import springboottemplate.data_services.comment.model.CommentDTO;
import springboottemplate.data_services.comment.service.CommentService;
import springboottemplate.data_services.post.model.Post;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Comment comment;

    @Mock
    private Post post;

    @BeforeEach
    void setUp() {
        commentController = new CommentController(commentService);
    }

    @Test
    void findCommentsByPosts_returnsExpectedComments() {
        CommentDTO commentDTO = new CommentDTO();
        when(commentService.findCommentsByPosts(post))
                .thenReturn(Collections.singletonList(commentDTO));

        List<CommentDTO> result = commentController.findCommentsByPosts(post);

        assertEquals(1, result.size());
        assertEquals(commentDTO, result.get(0));
    }

    @Test
    void addComment_returnsOkStatus() {
        CommentDTO commentDTO = new CommentDTO();
        when(commentService.createComment(userDetails, comment)).thenReturn(commentDTO);

        ResponseEntity<CommentDTO> result = commentController.addComment(userDetails, comment);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(commentDTO, result.getBody());
    }

    @Test
    void addComment_returnsInternalServerErrorStatus() {
        when(commentService.createComment(userDetails, comment)).thenThrow(new RuntimeException());

        ResponseEntity<CommentDTO> result = commentController.addComment(userDetails, comment);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}
