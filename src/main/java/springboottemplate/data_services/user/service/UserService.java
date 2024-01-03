package springboottemplate.data_services.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.*;
import springboottemplate.data_services.friendship.FriendshipRepository;
import springboottemplate.data_services.friendship.FriendshipStatus;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.UserDTO;
import springboottemplate.data_services.user.repository.UserRepository;
import springboottemplate.utilities.EmailValidationService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailValidationService emailValidationService;
    private final UserDTOMapper userDTOMapper;
    private final FriendshipRepository friendshipRepository;

    //GET
    public List<UserDTO> findFriendsByUserDetails(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);

        return friendshipRepository
                .getFriendshipByUserAndFriendshipStatusEquals(
                        user,
                        FriendshipStatus.ACCEPTED)
                .stream()
                .map(friendship -> userDTOMapper.apply(friendship.getFriend()))
                .toList();
    }
    public List<UserDTO> findFriendRequestsSentByUserDetails(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);

        return friendshipRepository
                .getFriendshipByUserAndFriendshipStatusEquals(
                        user, FriendshipStatus.PENDING
                )
                .stream()
                .map(friendship -> userDTOMapper.apply(friendship.getFriend()))
                .toList();
    }
    public List<UserDTO> findFriendRequestsSentToUserDetails(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);

        return friendshipRepository
                .getFriendshipByUserAndFriendshipStatusEquals(
                        user, FriendshipStatus.PENDING
                )
                .stream()
                .map(friendship -> userDTOMapper.apply(friendship.getUser()))
                .toList();
    }
    public List<UserDTO> searchUserByName(String query) {
        return userRepository.searchByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(userDTOMapper)
                .toList();
    }


    //POST


    //PUT
    public UserDTO updateUser(UserDetails userDetails, User userToUpdate) throws UserNotFoundException, EntityShouldContainAnId, UserDoesNotOwnEntityException {
        if (userToUpdate.getUserId() == null)
            throw new EntityShouldContainAnId(User.class);

        User userFromDb = getUser(userDetails);

        if (!Objects.equals(userToUpdate.getUserId(), userFromDb.getUserId()))
            throw new UserDoesNotOwnEntityException(userFromDb, User.class);

        User updatedUser = userRepository.save(userToUpdate);
        return userDTOMapper.apply(updatedUser);
    }


    //DELETE
    public void deleteUser(UserDetails userDetails) throws UserNotFoundException {

        User user = getUser(userDetails);
        userRepository.deleteById(user.getUserId());
    }

    //Utility
    private User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }
}
