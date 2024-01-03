package springboottemplate.data_services.friendship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboottemplate.data_services.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    List<Friendship> getFriendshipByUserAndFriendshipStatusEquals(User user, FriendshipStatus friendshipStatus);
    List<Friendship> getFriendshipByFriendAndFriendshipStatusEquals(User user, FriendshipStatus friendshipStatus);
    Optional<Friendship> getFriendshipByUserAndFriendAndFriendshipStatusEquals(User user, User friend, FriendshipStatus friendshipStatus);

}
