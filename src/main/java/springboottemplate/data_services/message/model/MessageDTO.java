package springboottemplate.data_services.message.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.model.UserDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
