package springboottemplate.data_services.friendship;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User friend;

    @NotNull
    @Column(nullable = false)
    private FriendshipStatus friendshipStatus;
}