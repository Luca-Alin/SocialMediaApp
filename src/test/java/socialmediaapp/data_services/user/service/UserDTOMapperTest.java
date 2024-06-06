package socialmediaapp.data_services.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import socialmediaapp.data_services.user.model.User;
import socialmediaapp.data_services.user.model.UserDTO;
import socialmediaapp.data_services.user.model.UserProfile;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserDTOMapperTest {

    private final UserDTOMapper userDTOMapper = new UserDTOMapper();

    @Test
    void UserDTOMapper_apply_returnsUserDTO() {
        //Arrange
        String uuid = UUID.randomUUID().toString();
        String email = "test@test.com";
        String password = "password";
        String firstName = "First Name";
        String lastName = "Last Name";
        UserProfile userProfile = new UserProfile();

        User user = User
                .builder()
                .uuid(uuid)
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .userProfile(userProfile)
                .build();


        //Act
        UserDTO userDTO = userDTOMapper.apply(user);

        //Assert
        assertThat(userDTO.getUuid()).isEqualTo(uuid);
        assertThat(userDTO.getFirstName()).isEqualTo(firstName);
        assertThat(userDTO.getLastName()).isEqualTo(lastName);
    }
}