package socialmediaapp.data_services.group.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import socialmediaapp.data_services.group.model.enums.GroupRank;
import socialmediaapp.data_services.user.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity(name = "GroupUser")
@Table(name = "group_users")
public class GroupUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_user_id")
    private Integer id;

    private GroupRank groupRank;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @ManyToOne
    private User user;
}
