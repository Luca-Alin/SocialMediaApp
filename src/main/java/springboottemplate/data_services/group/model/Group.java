package springboottemplate.data_services.group.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import springboottemplate.data_services.group.model.enums.HowToJoin;
import springboottemplate.data_services.group.model.enums.WhoCanCreatePosts;
import springboottemplate.data_services.group.model.enums.WhoCanSeePosts;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String groupName;
    private String groupDescription;
    private byte[] groupImage;

    private WhoCanCreatePosts whoCanCreatePosts;
    private WhoCanSeePosts whoCanSeePosts;
    private HowToJoin howToJoin;

    @OneToMany
    @JsonManagedReference
    private List<GroupUser> groupUsers;

    @OneToMany
    @JsonManagedReference
    private List<GroupPost> groupPosts;

    @OneToMany
    private List<GroupUser> bannedUsers;

    public List<GroupUser> getGroupUsers() {
        if (groupUsers == null) groupUsers = new ArrayList<>();
        return groupUsers;
    }

    public List<GroupUser> getBannedUsers() {
        if (bannedUsers == null) bannedUsers = new ArrayList<>();
        return bannedUsers;
    }

    public List<GroupPost> getGroupPosts() {
        if (groupPosts == null) groupPosts = new ArrayList<>();
        return groupPosts;
    }
}
