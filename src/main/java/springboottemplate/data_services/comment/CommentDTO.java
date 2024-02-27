package springboottemplate.data_services.comment;

import jakarta.persistence.*;
import lombok.*;
import springboottemplate.data_services.post.Post;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

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
