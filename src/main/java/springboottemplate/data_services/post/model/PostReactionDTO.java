package springboottemplate.data_services.post.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReactionDTO {
    private String userId;
    private PostReactionType postReactionType;
}
