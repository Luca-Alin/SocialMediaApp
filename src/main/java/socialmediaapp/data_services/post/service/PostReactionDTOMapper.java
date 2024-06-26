package socialmediaapp.data_services.post.service;


import org.springframework.stereotype.Service;
import socialmediaapp.data_services.post.model.PostReaction;
import socialmediaapp.data_services.post.model.PostReactionDTO;

import java.util.function.Function;

@Service
public class PostReactionDTOMapper implements Function<PostReaction, PostReactionDTO> {
    @Override
    public PostReactionDTO apply(PostReaction postReaction) {
        return PostReactionDTO
                .builder()
                .userId(postReaction.getUser().getUuid())
                .postReactionType(postReaction.getReaction())
                .build();
    }
}
