package springboottemplate.data_services.post;

import lombok.*;
import springboottemplate.data_services.comment.Comment;
import springboottemplate.data_services.comment.CommentDTO;
import springboottemplate.data_services.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class
PostDTO {
    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private UserDTO user;
    private List<byte[]> images;
    private List<CommentDTO> comments;
}
