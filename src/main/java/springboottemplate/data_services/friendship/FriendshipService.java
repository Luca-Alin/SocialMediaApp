package springboottemplate.data_services.friendship;

import com.github.javafaker.Friends;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public void sendFriendshipRequest(UserDetails userDetails, User receivingUser) throws UserNotFoundException, EntityShouldContainAnId {
        if (receivingUser.getUserId() == null) throw new EntityShouldContainAnId(User.class);

        User sendingUser = getUser(userDetails);

        Friendship friendship = Friendship
                .builder()
                .user(sendingUser)
                .friend(receivingUser)
                .friendshipStatus(FriendshipStatus.PENDING)
                .build();

        friendshipRepository.save(friendship);
    }

    public void acceptFriendshipRequest(UserDetails userDetails, User sendingUser) throws EntityShouldContainAnId, UserNotFoundException {
        if (sendingUser.getUserId() == null) throw new EntityShouldContainAnId(User.class);

        User receivingUser = getUser(userDetails);

        Friendship friendship1 = friendshipRepository.getFriendshipByUserAndFriendAndFriendshipStatusEquals(receivingUser, sendingUser, FriendshipStatus.PENDING)
                .orElseThrow();

        friendship1.setFriendshipStatus(FriendshipStatus.ACCEPTED);

        Friendship friendship2 = Friendship
                .builder()
                .user(sendingUser)
                .friend(receivingUser)
                .friendshipStatus(FriendshipStatus.ACCEPTED)
                .build();

        friendshipRepository.saveAll(List.of(friendship1, friendship2));
    }

    public void rejectFriendshipRequest(UserDetails userDetails, User sendingUser) throws EntityShouldContainAnId, UserNotFoundException {
        if (sendingUser.getUserId() == null) throw new EntityShouldContainAnId(User.class);

        User receivingUser = getUser(userDetails);

        Friendship friendship = friendshipRepository.getFriendshipByUserAndFriendAndFriendshipStatusEquals(receivingUser, sendingUser, FriendshipStatus.PENDING)
                .orElseThrow();

        friendshipRepository.delete(friendship);
    }

    public void deleteFriendship(UserDetails userDetails, User sendingUser) {
        throw new RuntimeException("Not implemented XDDD");
    }

    public User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }
    public User getUser(User user) throws UserNotFoundException {
        return userRepository.findById(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));
    }
}
