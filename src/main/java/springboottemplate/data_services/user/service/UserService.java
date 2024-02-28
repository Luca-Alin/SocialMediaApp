package springboottemplate.data_services.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.UserDoesNotOwnEntityException;
import springboottemplate.data_services.friendship.repository.FriendshipRepository;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.model.UserDTO;
import springboottemplate.data_services.user.exceptions.UserNotFoundException;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.*;

@RequiredArgsConstructor

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final FriendshipRepository friendshipRepository;

    public List<UserDTO> findFriendsByUserDetails(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);

        List<User> friendships = friendshipRepository
                .findCurrentFriendships(user)
                .stream()
                .map(f -> {
                    if (Objects.equals(f.getSender().getUuid(), user.getUuid()))
                        return f.getReceiver();
                    return f.getSender();
                }).toList();

        return friendships.stream()
                .map(userDTOMapper)
                .toList();
    }

    public List<UserDTO> findReceivedFriendshipRequestsByUserDetails(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);

        List<User> friendships = friendshipRepository
                .findReceivedFriendshipRequests(user)
                .stream()
                .map(f -> {
                    if (Objects.equals(f.getSender().getUuid(), user.getUuid()))
                        return f.getReceiver();
                    return f.getSender();
                }).toList();

        return friendships.stream()
                .map(userDTOMapper)
                .toList();

    }


    public List<UserDTO> searchUserByName(String query) throws InvalidSearchQueryException {
        if (query.isEmpty())
            throw new InvalidSearchQueryException();

        return userRepository.searchByQueryString(query)
                .stream()
                .map(userDTOMapper)
                .toList();
    }

    public UserDTO findUserById(String uuid) throws UserNotFoundException {
        return userRepository.findById(uuid)
                .map(userDTOMapper)
                .orElseThrow(() -> new UserNotFoundException("Email not found"));
    }

    public UserDTO updateUser(UserDetails userDetails, User userToUpdate) throws UserNotFoundException, EntityShouldContainAnId, UserDoesNotOwnEntityException {
        if (userToUpdate.getUuid() == null)
            throw new EntityShouldContainAnId(User.class);

        User userFromDb = getUser(userDetails);

        if (!Objects.equals(userToUpdate.getEmail(), userFromDb.getEmail()))
            throw new UserDoesNotOwnEntityException(userFromDb, User.class);

        User updatedUser = userRepository.save(userToUpdate);
        return userDTOMapper.apply(updatedUser);
    }


    public void deleteUser(UserDetails userDetails, String id) throws UserNotFoundException {
        User user = getUser(userDetails);

        if (!Objects.equals(user.getUuid(), id))
            throw new UserNotFoundException("User not found");

        userRepository.deleteById(id);
    }

    private User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }

    public UserDTO findUserByUserDetails(UserDetails userDetails) throws UserNotFoundException {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));

        return userDTOMapper.apply(user);
    }

    //    public List<UserDTO> getReceivedFriendshipRequests(UserDetails userDetails) throws UserNotFoundException {
//        User user = getUser(userDetails);
//
//        return friendshipRepository
//                .findReceivedFriendshipRequests(user)
//                .parallelStream()
//                .map(f -> {
//                    if (user.getId().equals(f.getSender().getId()))
//                        return f.getReceiver();
//                    return f.getSender();
//                })
//                .map(userDTOMapper)
//                .toList();
//    }
}
