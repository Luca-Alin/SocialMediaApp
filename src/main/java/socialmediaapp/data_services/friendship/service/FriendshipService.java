package springboottemplate.data_services.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.EntityDoesNotExistException;
import springboottemplate.data_services.friendship.exception.FriendshipAlreadyExists;
import springboottemplate.data_services.friendship.model.Friendship;
import springboottemplate.data_services.friendship.model.FriendshipStatus;
import springboottemplate.data_services.friendship.repository.FriendshipRepository;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.exceptions.UserNotFoundException;
import springboottemplate.data_services.user.repository.UserRepository;

@RequiredArgsConstructor

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    public FriendshipStatus checkFriendshipStatus(UserDetails userDetails, User receivingUser) throws UserNotFoundException {
        User user = getUser(userDetails);

        return friendshipRepository
                .findFriendshipStatus(user, receivingUser)
                .orElse(FriendshipStatus.NONE);
    }

    public FriendshipStatus sendFriendshipRequest(UserDetails userDetails, User receiver) throws UserNotFoundException, FriendshipAlreadyExists {
        User sender = getUser(userDetails);

        FriendshipStatus friendshipStatus = friendshipRepository
                .findFriendshipStatus(sender, receiver)
                .orElse(FriendshipStatus.NONE);

        if (friendshipStatus != FriendshipStatus.NONE)
            throw new FriendshipAlreadyExists();

        Friendship friendship = Friendship
                .builder()
                .sender(sender)
                .receiver(receiver)
                .friendshipStatus(FriendshipStatus.SENT)
                .build();

        return friendshipRepository.save(friendship).getFriendshipStatus();
    }

    public FriendshipStatus acceptFriendshipRequest(UserDetails receiverDetails, User sender) throws UserNotFoundException, EntityDoesNotExistException {
        User receiver = getUser(receiverDetails);

        Friendship friendship = friendshipRepository.findFriendshipBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new EntityDoesNotExistException(Friendship.class));
        friendship.setFriendshipStatus(FriendshipStatus.ACCEPTED);

        return friendshipRepository.save(friendship).getFriendshipStatus();
    }

    public void deleteFriendship(UserDetails userDetails, User friend) throws UserNotFoundException {
        User user = getUser(userDetails);

        friendshipRepository.deleteByBothUsers(user, friend);
    }




    public User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
