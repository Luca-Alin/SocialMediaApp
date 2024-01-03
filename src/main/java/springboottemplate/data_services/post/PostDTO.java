package springboottemplate.data_services.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboottemplate.data_services.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder

public class PostDTO {
    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private UserDTO user;
    private List<byte[]> images;
}
