package springboottemplate.data_services.group.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import springboottemplate.data_services.group.model.enums.GroupStatus;
import springboottemplate.data_services.user.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity
public class GroupUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private GroupStatus groupStatus;

    @ManyToOne
    @JsonBackReference
    private Group group;

    @ManyToOne
    private User user;
}
