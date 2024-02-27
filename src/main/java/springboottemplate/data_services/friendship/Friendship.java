package springboottemplate.data_services.friendship;

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
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User friend;

    @Column(nullable = false)
    private FriendshipStatus friendshipStatus;
}