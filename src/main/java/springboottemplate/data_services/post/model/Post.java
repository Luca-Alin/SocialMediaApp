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
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String uuid;

    @OneToMany(fetch = EAGER, mappedBy = "post", orphanRemoval = true)
    @JsonManagedReference
    List<Comment> comments;

    @Column(length = 4000)
    private String content;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PostImage> images;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PostReaction> postReactions = new ArrayList<>();

    public List<Comment> getComments() {
        if (comments == null) return comments = new ArrayList<>();
        return comments;
    }
    public List<PostImage> getImages() {
        if (images == null) return images = new ArrayList<>();
        return images;
    }
    public List<PostReaction> getPostReactions() {
        if (postReactions == null) return postReactions = new ArrayList<>();
        return postReactions;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = new Date();
    }
}
