package springboottemplate.data_services.message;

import lombok.*;
import springboottemplate.data_services.user.UserDTO;

/**
 * DTO for {@link Message}
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MessageDTO {
    private Integer id;
    private String content;
    private UserDTO sendingUser;
    private UserDTO receivingUser;
}
