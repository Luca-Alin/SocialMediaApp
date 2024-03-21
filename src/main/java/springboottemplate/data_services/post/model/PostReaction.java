package springboottemplate.data_services.post.model;

import jakarta.persistence.*;
import lombok.*;
import springboottemplate.data_services.user.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Embeddable
public class PostReaction {

    @ManyToOne
    private User user;

    private PostReactionType reaction;
}
