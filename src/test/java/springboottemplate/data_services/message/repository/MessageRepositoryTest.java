package springboottemplate.data_services.message.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springboottemplate.data_services.message.model.Message;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findConversationBetweenTwoUsers() {
        //Arrange
        String user1Id = "u1";
        String user2Id = "u2";
        String user3Id = "u3";
        String user4Id = "u4";
        /* the message repository will contain:
            - 2 message between user1 - user2
            - 1 message between user1 - user3
            - 1 message between user1 - user4
        */
        messageRepository.saveAll(List.of(
                Message.builder().senderId(user1Id).receiverId(user2Id).build(),
                Message.builder().senderId(user2Id).receiverId(user1Id).build(),
                Message.builder().senderId(user1Id).receiverId(user3Id).build(),
                Message.builder().senderId(user1Id).receiverId(user4Id).build()
        ));

        //Act
        List<Message> expect2messagesA = messageRepository.findConversationBetweenTwoUsers(user1Id, user2Id);
        List<Message> expect2messagesB = messageRepository.findConversationBetweenTwoUsers(user2Id, user1Id);

        List<Message> expect1messageA = messageRepository.findConversationBetweenTwoUsers(user1Id, user3Id);
        List<Message> expect1messageB = messageRepository.findConversationBetweenTwoUsers(user1Id, user4Id);

        //Assert
        assertThat(expect2messagesA).size().isEqualTo(2);
        assertThat(expect2messagesB).size().isEqualTo(2);

        assertThat(expect1messageA).size().isEqualTo(1);
        assertThat(expect1messageB).size().isEqualTo(1);
    }
}