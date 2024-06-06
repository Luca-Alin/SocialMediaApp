package socialmediaapp.data_services.post.model;

import lombok.*;
import socialmediaapp.data_services.comment.model.CommentDTO;
import socialmediaapp.data_services.user.model.UserDTO;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PostDTO {
    private String uuid;
    private String content;
    private Date createdAt;
    private UserDTO user;

    private List<byte[]> images;
    private List<CommentDTO> comments;
    private List<PostReactionDTO> postReactions;
}
