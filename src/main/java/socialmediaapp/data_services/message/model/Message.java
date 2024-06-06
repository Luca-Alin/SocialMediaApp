package socialmediaapp.data_services.message.model;


import jakarta.persistence.*;
import lombok.*;
import socialmediaapp.data_services.user.model.User;

import java.util.Date;

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

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private String content;

    private Boolean messageWasRead;

    private Date dateSent;

    @PrePersist
    void setDefaults() {
        if (messageWasRead == null)
            setMessageWasRead(false);
        if (dateSent == null)
            dateSent = new Date();
    }
}
