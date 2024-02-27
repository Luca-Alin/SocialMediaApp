package springboottemplate.data_services.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.comment.CommentDTO;
import springboottemplate.data_services.comment.service.CommentDTOMapper;
import springboottemplate.data_services.post.Post;
import springboottemplate.data_services.post.PostImage;
import springboottemplate.data_services.post.PostDTO;
import springboottemplate.data_services.user.service.UserDTOMapper;

import java.util.function.Function;

@RequiredArgsConstructor

@Service
public class PostDTOMapper implements Function<Post, PostDTO>
{
    private final UserDTOMapper userDTOMapper;
    private final CommentDTOMapper commentDTOMapper;
    @Override
    public PostDTO apply(Post post) {
        return PostDTO
                .builder()
                .id(post.getId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .user((post.getUser() == null) ? null : userDTOMapper.apply(post.getUser()))
                .images((post.getImages() == null) ? null : post.getImages().stream().map(PostImage::getImage).toList())
                .comments((post.getComments() == null) ? null : post.getComments().stream().map(commentDTOMapper).toList())
                .build();
    }
}
