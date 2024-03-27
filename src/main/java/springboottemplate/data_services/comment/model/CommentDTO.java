package springboottemplate.data_services.comment.model;

import lombok.*;
import springboottemplate.data_services.user.model.UserDTO;

import java.time.LocalDateTime;
import java.util.Date;

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
    private Date createdAt;
}
