package springboottemplate.data_services.comment.model;

import lombok.*;
import springboottemplate.data_services.user.model.UserDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Integer id;
    private String content;
    private UserDTO user;
    private LocalDateTime createdAt;
}
