package springboottemplate.data_services.message.model;

import lombok.*;
import springboottemplate.data_services.user.model.UserDTO;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {

    private UserDTO friend;
    private List<MessageDTO> messages;
}
