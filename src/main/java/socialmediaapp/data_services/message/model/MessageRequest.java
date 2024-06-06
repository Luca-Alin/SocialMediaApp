package socialmediaapp.data_services.message.model;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

        private String content;
        private String receiverId;
}
