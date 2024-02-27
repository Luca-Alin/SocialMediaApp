package springboottemplate.data_services.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.UserDoesNotOwnEntityException;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.UserDTO;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDTOMapper userDTOMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void UserService_FindFriendsByUserDetails_returnsFriends() {

    }

    @Test
    void findFriendRequestsSentByUserDetails() {
    }

    @Test
    void findFriendRequestsSentToUserDetails() {
    }

    @Test
    void testSearchUserByName() {
    }

    @Test
    void UserService_findUserById_returnsUserDTO() throws UserNotFoundException {
        //Arrange
        String firstName = "John";
        String lastName = "Smith";
        String email = "john_smith@test.com";
        String password = "password";

        User user = User
                .builder()
                .email(email).firstName(firstName).lastName(lastName).password(password)
                .build();
        UserDTO userDTO = UserDTO
                .builder().email(email).firstName(firstName).lastName(lastName)
                .build();
        //Act
        Mockito.when(userRepository.findById(Mockito.any(Integer.class)))
                .thenReturn(Optional.of(user));
        Mockito.when(userDTOMapper.apply(Mockito.any(User.class)))
                .thenReturn(userDTO);
        UserDTO foundUser = userService.findUserById(1);

        //Assert
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(userDTO.getEmail());
    }

    @Test
    void UserService_UpdateUser_returnsUserDTO() throws UserNotFoundException, EntityShouldContainAnId, UserDoesNotOwnEntityException {
        //Arrange
        String firstName = "John";
        String updatedFirstName = "John-Barrack";
        String lastName = "Smith";
        String email = "john_smith@test.com";
        String password = "password";

        User user = User
                .builder()
                .email(email).firstName(firstName).lastName(lastName).password(password)
                .build();
        User userToUpdate = User
                .builder()
                .id(1).email(email).firstName(updatedFirstName).lastName(lastName).password(password)
                .build();
        UserDTO updatedUserDTO = UserDTO
                .builder().id(1).email(email).firstName(updatedFirstName).lastName(lastName)
                .build();
        UserDetails userDetails = User
                .builder()
                .email(email)
                .password(password)
                .build();

        //Act
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class)))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(userToUpdate);
        Mockito.when(userDTOMapper.apply(Mockito.any(User.class)))
                .thenReturn(updatedUserDTO);
        //Assert
        Assertions.assertThat(userService.updateUser(userDetails, userToUpdate)).isEqualTo(updatedUserDTO);
    }

    @Test
    void UserService_deleteUser_returnsVoid() {
    }

    @Test
    void UserService_findUserByUserDetails_returnsUser() throws UserNotFoundException {
        //Arrange
        String firstName = "John";
        String lastName = "Smith";
        String email = "john_smith@test.com";
        String password = "password";

        User user = User
                .builder()
                .email(email).firstName(firstName).lastName(lastName).password(password)
                .build();
        UserDTO userDTO = UserDTO
                .builder().email(email).firstName(firstName).lastName(lastName)
                .build();
        UserDetails userDetails = User
                .builder()
                .email(email).password(password)
                .build();
        //Act
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class)))
                .thenReturn(Optional.of(user));
        Mockito.when(userDTOMapper.apply(Mockito.any(User.class)))
                .thenReturn(userDTO);
        UserDTO foundUser = userService.findUserByUserDetails(userDetails);

        //Assert
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(userDTO.getEmail());
    }
}