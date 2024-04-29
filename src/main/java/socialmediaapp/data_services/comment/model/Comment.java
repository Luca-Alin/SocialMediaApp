package springboottemplate.data_services.comment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import springboottemplate.data_services.post.model.Post;
import springboottemplate.data_services.user.model.User;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 4000)
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "post_id")
    @JsonBackReference
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = new Date();
    }
}
