package springboottemplate.data_services.message.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String senderId;

    private String receiverId;

    private String content;

    /*
    JSON for testing with postman
    {
        "id": null,
        "senderId": "uuid",
        "receiverId": "uuid",
        "content": "This is a fabulous text message"
    }
    */
}
