package springboottemplate.data_services.message.model;


import jakarta.persistence.*;
import lombok.*;
import springboottemplate.data_services.user.model.User;

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

    private boolean messageWasRead;

    private Date dateSent;

    @PrePersist
    void setDefaults() {
        setMessageWasRead(false);
        setDateSent(new Date());
    }
}
