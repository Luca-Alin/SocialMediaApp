package springboottemplate.data_services.message.repository;

import org.aspectj.weaver.ast.Var;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class MessageRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeAll
    void createUsers() {
        User user1 = User.builder().firstName("John").lastName("Doe").email("test1@test.com").password("password").build();
        User user2 = User.builder().firstName("John").lastName("Smith").email("test2@test.com").password("password").build();
        User user3 = User.builder().firstName("Jane").lastName("Doe").email("test3@test.com").password("password").build();
        User user4 = User.builder().firstName("Jane").lastName("Smith").email("test4@test.com").password("password").build();
        userRepository.saveAll(List.of(user1, user2, user3, user4));
    }


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void MessageRepository_save_returnsMessage() {
        List<User> users = userRepository.findAll();

        Message message = Message
                .builder()
                .content("Text")
                .sender(users.get(0))
                .receiver(users.get(1))
                .build();

        Message savedMessage = messageRepository.save(message);

        assertThat(savedMessage.getContent()).isEqualTo("Text");
        assertThat(savedMessage.getSender()).isEqualTo(users.get(0));
        assertThat(savedMessage.getReceiver()).isEqualTo(users.get(1));
    }

    @Test
    void findMessagesSendOrReceivedByUser() {
        List<User> users = userRepository.findAll();
        User john = users.get(0);
        User smith = users.get(1);
        User josh = users.get(2);
        User johnston = users.get(3);

        Message message1 = Message.builder().sender(john).receiver(smith).build();
        Message message2 = Message.builder().sender(smith).receiver(john).build();
        Message message3 = Message.builder().sender(john).receiver(josh).build();
        Message message4 = Message.builder().sender(josh).receiver(johnston).build();
        messageRepository.saveAll(List.of(message1, message2, message3, message4));


        List<Message> messagesOfJohn = messageRepository.findMessagesSendOrReceivedByUser(john);

        assertThat(messagesOfJohn).size().isEqualTo(3);
        assertThat(messagesOfJohn).allMatch((message) ->
                john.equals(message.getSender()) || john.equals(message.getReceiver())
        );
    }
}