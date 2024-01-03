package springboottemplate.data_services.user.service;

import org.springframework.stereotype.Service;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.UserDTO;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return UserDTO
                .builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profileImage(user.getUserProfile().getProfileImage())
                .build();
    }
}
