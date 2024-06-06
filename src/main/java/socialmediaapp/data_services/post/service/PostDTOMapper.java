package socialmediaapp.data_services.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import socialmediaapp.data_services.comment.service.CommentDTOMapper;
import socialmediaapp.data_services.post.model.Post;
import socialmediaapp.data_services.post.model.PostImage;
import socialmediaapp.data_services.post.model.PostDTO;
import socialmediaapp.data_services.user.service.UserDTOMapper;

import java.util.ArrayList;
import java.util.function.Function;

@RequiredArgsConstructor

@Service
public class PostDTOMapper implements Function<Post, PostDTO> {
    private final PostReactionDTOMapper postReactionDTOMapper;
    private final UserDTOMapper userDTOMapper;
    private final CommentDTOMapper commentDTOMapper;
    @Override
    public PostDTO apply(Post post) {
        return PostDTO
                .builder()
                .uuid(post.getUuid())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .user((post.getUser() == null) ? null : userDTOMapper.apply(post.getUser()))
                .images((post.getImages() == null) ? null : post.getImages().stream().map(PostImage::getImage).toList())
                .comments((post.getComments() == null) ? null : post.getComments().stream().map(commentDTOMapper).toList())
                .postReactions(
                        (post.getPostReactions() == null) ?
                                new ArrayList<>() :
                                post.getPostReactions()
                                        .stream()
                                        .map(postReactionDTOMapper)
                                        .toList()
                        )
                .build();
    }
}
