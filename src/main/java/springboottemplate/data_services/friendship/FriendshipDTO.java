package springboottemplate.data_services.friendship;

import lombok.*;
import springboottemplate.data_services.user.UserDTO;

import java.io.Serializable;

/**
 * DTO for {@link Friendship}
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FriendshipDTO implements Serializable {
    Integer id;
    UserDTO user;
    UserDTO friend;
    FriendshipStatus friendshipStatus;
}