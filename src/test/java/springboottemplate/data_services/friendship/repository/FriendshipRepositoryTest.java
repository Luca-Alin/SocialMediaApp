package springboottemplate.data_services.friendship.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springboottemplate.data_services.friendship.model.Friendship;
import springboottemplate.data_services.friendship.model.FriendshipStatus;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Slf4j
class FriendshipRepositoryTest {

    @BeforeEach
    void setUp() {
        User user1 = User.builder().firstName("John").lastName("Doe").email("test1@test.com").password("password").build();
        User user2 = User.builder().firstName("John").lastName("Smith").email("test2@test.com").password("password").build();
        User user3 = User.builder().firstName("Jane").lastName("Doe").email("test3@test.com").password("password").build();
        User user4 = User.builder().firstName("Jane").lastName("Smith").email("test4@test.com").password("password").build();
        userRepository.saveAll(List.of(user1, user2, user3, user4));
    }

    @AfterEach
    void tearDown() {
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void FriendshipRepository_addFriendship_ReturnsFriendship() {
        //Arrange
        List<User> users = userRepository.findAll();
        var user1 = users.get(0);
        var user2 = users.get(users.size() - 1);

        Friendship friendship = Friendship.builder().sender(user1).receiver(user2).friendshipStatus(FriendshipStatus.SENT).build();

        //Act
        Friendship savedFriendship = friendshipRepository.save(friendship);

        //Assert
        assertThat(savedFriendship).isNotNull();
        assertThat(savedFriendship.getSender()).isEqualTo(user1);
        assertThat(savedFriendship.getReceiver()).isEqualTo(user2);
        assertThat(savedFriendship.getFriendshipStatus()).isEqualTo(FriendshipStatus.SENT);
    }

    @Test
    public void FriendshipRepository_findReceivedFriendshipRequests_ReturnsListOfFriends() {
        //Arrange
        List<User> users = userRepository.findAll();
        var user1 = users.get(0);
        var user2 = users.get(1);
        var user3 = users.get(2);
        var user4 = users.get(3);

        Friendship friendship1 = Friendship.builder().sender(user2).receiver(user1).friendshipStatus(FriendshipStatus.SENT).build();
        Friendship friendship2 = Friendship.builder().sender(user3).receiver(user1).friendshipStatus(FriendshipStatus.SENT).build();
        Friendship friendship3 = Friendship.builder().sender(user4).receiver(user1).friendshipStatus(FriendshipStatus.SENT).build();

        //Act
        friendshipRepository.saveAll(List.of(friendship1, friendship2, friendship3));
        List<Friendship> user1ReceivedFriendshipRequests = friendshipRepository.findReceivedFriendshipRequests(user1);

        //Assert
        assertThat(user1ReceivedFriendshipRequests).size().isEqualTo(3);
    }

    @Test
    public void FriendshipRepository_findSentFriendshipRequests_ReturnsListOfFriends() {
        //Arrange
        List<User> users = userRepository.findAll();
        var user1 = users.get(0);
        var user2 = users.get(1);
        var user3 = users.get(2);
        var user4 = users.get(3);

        Friendship friendship1 = Friendship.builder().sender(user1).receiver(user2).friendshipStatus(FriendshipStatus.SENT).build();
        Friendship friendship2 = Friendship.builder().sender(user1).receiver(user3).friendshipStatus(FriendshipStatus.SENT).build();
        Friendship friendship3 = Friendship.builder().sender(user1).receiver(user4).friendshipStatus(FriendshipStatus.SENT).build();

        //Act
        friendshipRepository.saveAll(List.of(friendship1, friendship2, friendship3));
        List<Friendship> findSentFriendshipsUser1 = friendshipRepository.findSentFriendshipRequests(user1);
        List<Friendship> findSentFriendshipsUser2 = friendshipRepository.findSentFriendshipRequests(user2);

        //Assert
        assertThat(findSentFriendshipsUser1).size().isEqualTo(3);
        assertThat(findSentFriendshipsUser1).anyMatch(f -> f.getSender().equals(user1) && f.getReceiver().equals(user2));
        assertThat(findSentFriendshipsUser1).anyMatch(f -> f.getSender().equals(user1) && f.getReceiver().equals(user3));
        assertThat(findSentFriendshipsUser1).anyMatch(f -> f.getSender().equals(user1) && f.getReceiver().equals(user4));

        assertThat(findSentFriendshipsUser2).size().isEqualTo(0);
    }

    @Test
    public void FriendshipRepository_findCurrentFriendships_ReturnsListOfFriends() {
        //Arrange
        List<User> users = userRepository.findAll();
        var user1 = users.get(0);
        var user2 = users.get(1);
        var user3 = users.get(2);
        var user4 = users.get(3);

        Friendship friendship1 = Friendship.builder().sender(user2).receiver(user1).friendshipStatus(FriendshipStatus.ACCEPTED).build();
        Friendship friendship2 = Friendship.builder().sender(user3).receiver(user1).friendshipStatus(FriendshipStatus.ACCEPTED).build();

        //Act
        friendshipRepository.saveAll(List.of(friendship1, friendship2));

        List<Friendship> user1CurrentFriendships = friendshipRepository.findCurrentFriendships(user1);
        List<Friendship> user2CurrentFriendships = friendshipRepository.findCurrentFriendships(user2);
        List<Friendship> user4CurrentFriendships = friendshipRepository.findCurrentFriendships(user4);

        //Assert
        assertThat(user1CurrentFriendships).size().isEqualTo(2);
        assertThat(user2CurrentFriendships).size().isEqualTo(1);
        assertThat(user4CurrentFriendships).size().isEqualTo(0);
    }

    @Test
    public void FriendshipRepository_findFriendshipStatus_ReturnsListOfFriends() {
        //Arrange
        List<User> users = userRepository.findAll();
        var user1 = users.get(0);
        var user2 = users.get(1);
        var user3 = users.get(2);
        var user4 = users.get(3);

        Friendship friendship1 = Friendship.builder().sender(user1).receiver(user2).friendshipStatus(FriendshipStatus.ACCEPTED).build();
        Friendship friendship2 = Friendship.builder().sender(user3).receiver(user4).friendshipStatus(FriendshipStatus.SENT).build();

        //Act
        friendshipRepository.saveAll(List.of(friendship1, friendship2));

        FriendshipStatus friendshipStatusU1U2 = friendshipRepository.findFriendshipStatus(user1, user2).orElse(null);
        FriendshipStatus friendshipStatusU2U1 = friendshipRepository.findFriendshipStatus(user2, user1).orElse(null);

        FriendshipStatus friendshipStatusU3U4 = friendshipRepository.findFriendshipStatus(user3, user4).orElse(null);
        FriendshipStatus friendshipStatusU4U3 = friendshipRepository.findFriendshipStatus(user4, user3).orElse(null);

        FriendshipStatus friendshipStatusU1U4 = friendshipRepository.findFriendshipStatus(user1, user4).orElse(null);

        //Assert
        assertThat(friendshipStatusU1U2).isNotNull();
        assertThat(friendshipStatusU2U1).isNotNull();
        assertThat(friendshipStatusU3U4).isNotNull();
        assertThat(friendshipStatusU4U3).isNotNull();

        assertThat(friendshipStatusU1U4).isNull();
    }


}