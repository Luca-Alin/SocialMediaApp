package socialmediaapp.data_services.friendship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import socialmediaapp.data_services.friendship.model.Friendship;
import socialmediaapp.data_services.friendship.model.FriendshipStatus;
import socialmediaapp.data_services.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    /**
     * @param user a user that exists in the database
     * @return The list of friendship records where this user is the receiver of the request,
     * and where the friendship status is "sent"
     */
    @Query("select f from Friendship f where f.receiver = ?1 and f.friendshipStatus = 0")
    List<Friendship> findReceivedFriendshipRequests(User user);

    @Query("select f from Friendship f where f.sender = ?1 and f.friendshipStatus = 0")
    List<Friendship> findSentFriendshipRequests(User user);

    /**
     * @param user a user that exists in the database
     * @return The list of friendship records that contain
     * either as sender or receiver, this user, and where
     * the friendship status is "accepted"
     */
    @Query("    select f\n" +
           "    from Friendship f\n" +
           "    where f.sender = ?1\n" +
           "    or f.receiver = ?1\n" +
           "    and f.friendshipStatus = 1\n")
    List<Friendship> findCurrentFriendships(User user);

    @Query("    select f.friendshipStatus\n" +
           "    from Friendship f\n" +
           "    where (f.sender = ?1 and f.receiver = ?2)\n" +
           "    or (f.sender = ?2 or f.receiver = ?1)\n")
    Optional<FriendshipStatus> findFriendshipStatus(User user1, User user2);


    Optional<Friendship> findFriendshipBySenderAndReceiver(User sender, User receiver);


    /**
     * Deletes from the friendship a friendship sent by user1 and received by user2,
     * or vice versa
     * @param user1 - a user
     * @param user2 - another user
     */
    @Modifying
    @Query("    delete\n" +
           "    from Friendship f\n" +
           "    where (f.sender = ?1 and f.receiver = ?2)\n" +
           "    or (f.sender = ?2 and f.receiver = ?1)\n")
    void deleteByBothUsers(User user1, User user2);
}
