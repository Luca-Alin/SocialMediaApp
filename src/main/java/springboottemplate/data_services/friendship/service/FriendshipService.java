package springboottemplate.data_services.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.friendship.Friendship;
import springboottemplate.data_services.friendship.FriendshipDTO;
import springboottemplate.data_services.friendship.repository.FriendshipRepository;
import springboottemplate.data_services.friendship.FriendshipStatus;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final FriendshipDTOMapper friendshipDTOMapper;

    public FriendshipStatus checkFriendshipStatus(UserDetails userDetails, User receivingUser) throws UserNotFoundException {
        User sendingUser = getUser(userDetails);
        User receiving = userRepository.findById(receivingUser.getId()).orElseThrow();

        if (sendingUser.getId().equals(receiving.getId()))
            return FriendshipStatus.SAMEPERSON;

        return friendshipRepository.findFriendshipByUserAndFriend(sendingUser, receiving)
                .map(Friendship::getFriendshipStatus)
                .orElse(FriendshipStatus.NONE);
    }

    public FriendshipStatus sendFriendshipRequest(UserDetails userDetails, User receivingUser) throws UserNotFoundException, EntityShouldContainAnId {
        if (receivingUser.getId() == null) throw new EntityShouldContainAnId(User.class);

        User sendingUser = getUser(userDetails);

        // Check for existing friendships between the two users
        if (friendshipRepository.existsByUserAndFriend(sendingUser, receivingUser) ||
                friendshipRepository.existsByUserAndFriend(receivingUser, sendingUser)) {
            throw new IllegalStateException("A friendship already exists between these two users");
        }

        Friendship friendship1 = Friendship
                .builder()
                .user(sendingUser)
                .friend(receivingUser)
                .friendshipStatus(FriendshipStatus.SENT)
                .build();

        Friendship friendship2 = Friendship
                .builder()
                .user(receivingUser)
                .friend(sendingUser)
                .friendshipStatus(FriendshipStatus.RECEIVED)
                .build();

        friendshipRepository.saveAll(List.of(friendship1, friendship2));

        return FriendshipStatus.PENDING;
    }

    public void acceptFriendshipRequest(UserDetails userDetails, User sendingUser) throws EntityShouldContainAnId, UserNotFoundException {
        if (sendingUser.getId() == null) throw new EntityShouldContainAnId(User.class);

        User receivingUser = getUser(userDetails);

        Friendship friendship1 = friendshipRepository
                .getFriendshipByUserAndFriendAndFriendshipStatusEquals(receivingUser, sendingUser, FriendshipStatus.RECEIVED)
                .orElseThrow();
        friendship1.setFriendshipStatus(FriendshipStatus.ACCEPTED);

        Friendship friendship2 = friendshipRepository
                .getFriendshipByUserAndFriendAndFriendshipStatusEquals(sendingUser, receivingUser, FriendshipStatus.SENT)
                .orElseThrow();
        friendship2.setFriendshipStatus(FriendshipStatus.ACCEPTED);

        friendshipRepository.saveAll(List.of(friendship1, friendship2));
    }

    public List<FriendshipDTO> getReceivedFriendshipRequests(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);

        return friendshipRepository
                .getFriendshipByUserAndFriendshipStatusEquals(user, FriendshipStatus.RECEIVED)
                .stream()
                .map(friendshipDTOMapper)
                .toList();
    }

    public List<FriendshipDTO> getUsersFriendships(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);

        return friendshipRepository
                .getFriendshipByUserAndFriendshipStatusEquals(user, FriendshipStatus.ACCEPTED)
                .stream()
                .map(friendshipDTOMapper)
                .toList();
    }

    public User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }

}
