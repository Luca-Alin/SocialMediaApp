package springboottemplate.data_services.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboottemplate.data_services.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Post {
    @NotNull
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Integer id;

    private String content;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @Size(min = 0, max = 10)
    @ElementCollection(fetch = EAGER)
    private List<PostImage> images;

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
