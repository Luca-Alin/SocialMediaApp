package springboottemplate.data_services.message;

import jakarta.persistence.*;
import lombok.*;
import springboottemplate.data_services.user.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 4000)
    private String content;

    @ManyToOne
    private User sendingUser;

    @ManyToOne
    private User receivingUser;
}
