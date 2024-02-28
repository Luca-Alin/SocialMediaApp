package springboottemplate.data_services.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.comment.model.Comment;
import springboottemplate.data_services.comment.model.CommentDTO;
import springboottemplate.data_services.user.service.UserDTOMapper;

import java.util.function.Function;

@RequiredArgsConstructor

@Service
public class CommentDTOMapper implements Function<Comment, CommentDTO> {
    private final UserDTOMapper userDTOMapper;
    @Override
    public CommentDTO apply(Comment comment) {
        return CommentDTO
                .builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(userDTOMapper.apply(comment.getUser()))
                .build();
    }
}
