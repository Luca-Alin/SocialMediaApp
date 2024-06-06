package socialmediaapp.data_services.user.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String uuid;

    private String firstName;

    private String lastName;

    private String bio;

    private String email;


    private byte[] profileImage;
}
