package springboottemplate.data_services.message.model;

import jakarta.persistence.ManyToOne;
import lombok.*;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.model.UserDTO;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Integer id;
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime dateSent;
}
