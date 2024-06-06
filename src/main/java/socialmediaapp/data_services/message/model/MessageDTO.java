package socialmediaapp.data_services.message.model;

import lombok.*;

import java.util.Date;

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
    private Boolean messageWasRead;
    private Date dateSent;
}
