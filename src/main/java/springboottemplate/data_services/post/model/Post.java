package springboottemplate.data_services.post.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import springboottemplate.data_services.comment.model.Comment;
import springboottemplate.data_services.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String uuid;

    @OneToMany(fetch = EAGER, mappedBy = "post")
    @JsonManagedReference
    List<Comment> comments;

    @Column(length = 4000)
    private String content;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @ElementCollection(fetch = EAGER)
    private List<PostImage> images;

    @ElementCollection(fetch = EAGER)
    private List<PostReaction> postReactions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = new Date();
    }
}
