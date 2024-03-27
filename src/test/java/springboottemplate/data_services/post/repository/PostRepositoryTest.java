package springboottemplate.data_services.post.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import springboottemplate.data_services.friendship.model.Friendship;
import springboottemplate.data_services.friendship.model.FriendshipStatus;
import springboottemplate.data_services.friendship.repository.FriendshipRepository;
import springboottemplate.data_services.post.model.Post;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        friendshipRepository.deleteAllInBatch();

        var u1 = userRepository.save(User.builder().email("test1@test.com").firstName("John").lastName("Doe").password("password").build());
        var u2 = userRepository.save(User.builder().email("test2@test.com").firstName("Jane").lastName("Doe").password("password").build());
        var u3 = userRepository.save(User.builder().email("test3@test.com").firstName("John").lastName("Smith").password("password").build());
        var u4 = userRepository.save(User.builder().email("test4@test.com").firstName("Jane").lastName("Smith").password("password").build());

        var f1 = friendshipRepository.save(Friendship.builder().sender(u1).receiver(u2).friendshipStatus(FriendshipStatus.ACCEPTED).build());
        var f2 = friendshipRepository.save(Friendship.builder().sender(u1).receiver(u3).friendshipStatus(FriendshipStatus.ACCEPTED).build());
        var f3 = friendshipRepository.save(Friendship.builder().sender(u2).receiver(u3).friendshipStatus(FriendshipStatus.ACCEPTED).build());

        /* Current Friendships

            Friend1 <-> Friend2
            Friend1 <-> Friend3
            Friend2 <-> Friend3

                ?? Friend4 ??
        */
    }

    @AfterEach
    void tearDown() {
        List<Post> posts = postRepository.findAll();
        for (Post p : posts)
            postRepository.deleteById(p.getUuid());
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void PostRepository_save_ReturnsPost() {
        //Arrange
        User user = userRepository.findAll().get(0);


        String postContent = "Test Post";
        Post post = Post
                .builder()
                .user(user)
                .content(postContent)
                .build();

        //Act
        Post savedPost = postRepository.save(post);

        //Assert
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getCreatedAt()).isNotNull();
        assertThat(savedPost.getContent()).isEqualTo(postContent);
    }

    @Test
    public void PostRepository_findAllByUser_ReturnsPageableOfPostsByUser() {
        //Arrange
        List<User> users = userRepository.findAll();
        var p1 = Post.builder().user(users.get(0)).content("Test Post").build();
        var p2 = Post.builder().user(users.get(0)).content("Test Post").build();
        var p3 = Post.builder().user(users.get(0)).content("Test Post").build();
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        postRepository.saveAll(List.of(p1, p2, p3));

        List<Post> user1Posts = postRepository.findAllByUserOrderByCreatedAt(users.get(0), pageable);
        List<Post> user2Posts = postRepository.findAllByUserOrderByCreatedAt(users.get(1), pageable);
        List<Post> page0items1 = postRepository.findAllByUserOrderByCreatedAt(users.get(0), PageRequest.of(0, 1));
        List<Post> page1items1 = postRepository.findAllByUserOrderByCreatedAt(users.get(1), PageRequest.of(1, 1));

        //Assert
        assertThat(user1Posts).isNotNull();
        assertThat(user1Posts).size().isEqualTo(3);

        assertThat(user2Posts).isNotNull();
        assertThat(user2Posts).size().isEqualTo(0);

        assertThat(page0items1).isNotNull();
        assertThat(page0items1).size().isEqualTo(1);

        assertThat(page1items1).isNotNull();
        assertThat(page1items1).size().isEqualTo(0);
    }

    @Test
    public void PostRepository_findSelfAndFriendsPosts_ReturnsPageableOfOnlyPostsBySelfAndFriends() {
        //Arrange
        List<User> users = userRepository.findAll();
        var p1 = Post.builder().user(users.get(0)).content("Test Post").build();
        var p2 = Post.builder().user(users.get(0)).content("Test Post").build();
        var p3 = Post.builder().user(users.get(1)).content("Test Post").build();
        var p4 = Post.builder().user(users.get(2)).content("Test Post").build();
        var p5 = Post.builder().user(users.get(3)).content("Test Post").build();

        int pageNumber = 0, pagesize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pagesize);

        //Act
        postRepository.saveAll(List.of(p1, p2, p3, p4, p5));

        List<Post> user1Posts = postRepository.findAllPostsByUserAndHisFriends(users.get(0), pageable);
        List<Post> user2Posts = postRepository.findAllPostsByUserAndHisFriends(users.get(1), pageable);
        List<Post> user4Posts = postRepository.findAllPostsByUserAndHisFriends(users.get(3), pageable);

        List<Post> page0items2 = postRepository.findAllPostsByUserAndHisFriends(users.get(0), PageRequest.of(0, 2));
        List<Post> page1items2 = postRepository.findAllPostsByUserAndHisFriends(users.get(0), PageRequest.of(1, 2));
        List<Post> page2items0 = postRepository.findAllPostsByUserAndHisFriends(users.get(0), PageRequest.of(2, 2));


        //Assert
        assertThat(user1Posts).isNotNull();
        assertThat(user1Posts).size().isEqualTo(4);

        assertThat(user2Posts).isNotNull();
        assertThat(user2Posts).size().isEqualTo(4);

        assertThat(user4Posts).isNotNull();
        assertThat(user4Posts).size().isEqualTo(1);

        assertThat(page0items2).isNotNull();
        assertThat(page0items2).size().isEqualTo(2);

        assertThat(page1items2).isNotNull();
        assertThat(page1items2).size().isEqualTo(2);

        assertThat(page2items0).isNotNull();
        assertThat(page2items0).size().isEqualTo(0);
    }


}