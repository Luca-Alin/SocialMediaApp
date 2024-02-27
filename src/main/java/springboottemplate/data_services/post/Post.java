package springboottemplate.data_services.post;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import springboottemplate.data_services.comment.Comment;
import springboottemplate.data_services.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Post {
    @OneToMany(fetch = EAGER, mappedBy = "post")
    @JsonManagedReference
    List<Comment> comments;
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Integer id;
    @Column(length = 4000)
    private String content;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;
    @ElementCollection(fetch = EAGER)
    private List<PostImage> images;

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
