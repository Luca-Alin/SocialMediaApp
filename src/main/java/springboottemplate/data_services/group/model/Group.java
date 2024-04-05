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

@Entity(name = "Group")
@Table(name = "groups_table")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    private String id;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String groupDescription;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] groupImage;

    @Column(nullable = false)
    private WhoCanCreatePosts whoCanCreatePosts;

    @Column(nullable = false)
    private WhoCanSeePosts whoCanSeePosts;

    @Column(nullable = false)
    private HowToJoin howToJoin;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
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
