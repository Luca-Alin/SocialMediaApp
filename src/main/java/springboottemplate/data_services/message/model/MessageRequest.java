package springboottemplate.data_services.message.model;

import lombok.*;
import springboottemplate.data_services.user.model.User;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String content;
    private String receiverId;
}
