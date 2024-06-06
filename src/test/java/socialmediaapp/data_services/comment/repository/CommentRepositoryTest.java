package socialmediaapp.data_services.comment.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import socialmediaapp.data_services.comment.model.Comment;
import socialmediaapp.data_services.post.model.Post;
import socialmediaapp.data_services.post.repository.PostRepository;
import socialmediaapp.data_services.user.model.User;
import socialmediaapp.data_services.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void CommentRepository_findAllByPost_returnsComments() {
        //Arrange
        User user = userRepository.save(
                User.builder().email("john_smith@test.com").lastName("John1").firstName("Smith1").password("password").build()
        );
        Post post = postRepository.save(
                Post.builder().user(user).content("Test content").build()
        );
        Comment comment = commentRepository.save(
          Comment.builder().content("Test content").post(post).user(user).build()
        );

        //Act
        List<Comment> comments = commentRepository.findAllByPost(post);

        //Assert
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments).allMatch(cmt -> cmt.getUser().equals(user));
        assertThat(comments).allMatch(cmt -> cmt.getPost().equals(post));
    }
}