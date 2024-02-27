package springboottemplate.data_services.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private byte[] profileImage;
}
