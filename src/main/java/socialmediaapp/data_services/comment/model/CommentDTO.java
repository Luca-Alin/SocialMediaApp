package socialmediaapp.data_services.comment.model;

import lombok.*;
import socialmediaapp.data_services.user.model.UserDTO;

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
