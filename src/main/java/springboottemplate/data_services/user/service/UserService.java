package springboottemplate.data_services.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.UserDoesNotOwnEntityException;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.friendship.FriendshipStatus;
import springboottemplate.data_services.friendship.repository.FriendshipRepository;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.UserDTO;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.*;

@RequiredArgsConstructor

@Service
public class UserService {
    private final UserRepository userRepository;
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
        List<String> queryList = new ArrayList<>();
        Arrays.stream(query.split(" ")).forEach((t) -> {
            queryList.add(t);
            queryList.add(t.toLowerCase(Locale.ROOT));
            queryList.add(t.toUpperCase());
            queryList.add(t.toUpperCase().charAt(0) + t.toLowerCase().substring(1));
        });


        return userRepository.searchByFirstNameInOrLastNameIn(queryList, queryList)
                .stream()
                .map(userDTOMapper)
                .toList();
    }

    public UserDTO findUserById(Integer id) throws UserNotFoundException {
        return userRepository.findById(id)
                .map(userDTOMapper)
                .orElseThrow(() -> new UserNotFoundException("email not found"));
    }

    //POST


    //PUT
    public UserDTO updateUser(UserDetails userDetails, User userToUpdate) throws UserNotFoundException, EntityShouldContainAnId, UserDoesNotOwnEntityException {
        if (userToUpdate.getId() == null)
            throw new EntityShouldContainAnId(User.class);

        User userFromDb = getUser(userDetails);

        if (!Objects.equals(userToUpdate.getEmail(), userFromDb.getEmail()))
            throw new UserDoesNotOwnEntityException(userFromDb, User.class);

        User updatedUser = userRepository.save(userToUpdate);
        return userDTOMapper.apply(updatedUser);
    }


    //DELETE
    public void deleteUser(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);
        userRepository.deleteById(user.getId());
    }

    //Utility
    private User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }

    public UserDTO findUserByUserDetails(UserDetails userDetails) throws UserNotFoundException {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));

        return userDTOMapper.apply(user);
    }
}
