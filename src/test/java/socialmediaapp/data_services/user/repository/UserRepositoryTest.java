package socialmediaapp.data_services.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import socialmediaapp.data_services.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    public void UserRepository_Save_ReturnSavedUsers() {
        //Arrange
        String eml = "john_smith@test.com",
                ln = "John",
                fn = "Smith",
                pwd = "password";
        User user = User.builder().email(eml).lastName(ln).firstName(fn).password(pwd).build();

        //Act
        User savedUser = userRepository.save(user);

        //Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUuid().length()).isGreaterThan(0);
        assertThat(savedUser.getEmail()).isEqualTo(eml);
        assertThat(savedUser.getFirstName()).isEqualTo(fn);
        assertThat(savedUser.getLastName()).isEqualTo(ln);
        assertThat(savedUser.getPassword()).isEqualTo(pwd);
    }

    @Test
    public void UserRepository_SaveTwoUsersWithTheSameEmail_ThrowsException() {
        //Arrange
        User user1 = User.builder().email("john_smith@test.com").lastName("John1").firstName("Smith1").password("password").build();
        User user2 = User.builder().email("john_smith@test.com").lastName("John2").firstName("Smith2").password("password").build();

        //Act
        userRepository.saveAndFlush(user1);

        //Assert
        assertThatException().isThrownBy(() -> userRepository.saveAndFlush(user2));

        System.out.println("Hello, World!");
    }

    @Test
    public void UserRepository_SaveUserWithRequiredFieldsBeingNull_ThrowsException() {
        //Arrange
        User user = User.builder().firstName("Smith1").password("password").build();

        //Act and assert
        assertThatException().isThrownBy(() -> userRepository.save(user));
    }


    @Test
    public void UserRepository_GetAll_ReturnMoreThanOneUser() {
        //Arrange
        User user = User.builder().email("john_smith@test.com").lastName("John").firstName("Smith").password("password").build();
        User user2 = User.builder().email("john_doe@test.com").lastName("John").firstName("Doe").password("password").build();

        //Act
        userRepository.save(user);
        userRepository.save(user2);
        List<User> userList = userRepository.findAll();

        //Assert
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUser() {
        //Arrange
        User user = User.builder().email("john_smith@test.com").lastName("John").firstName("Smith").password("password").build();

        //Act
        User savedUser = userRepository.save(user);

        User userById = userRepository.findById(savedUser.getUuid())
                .orElseThrow();

        //Assert
        assertThat(userById).isNotNull();
    }

    @Test
    public void UserRepository_DeleteById_DeleteUser() {
        //Arrange
        User user = User.builder().email("john_smith@test.com").lastName("John").firstName("Smith").password("password").build();

        //Act
        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getUuid());

        List<User> userList = userRepository.findAll();

        //Assert
        assertThat(userList.size()).isEqualTo(0);
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUser() {
        //Arrange
        String email = "john_smith@test.com";
        User user = User.builder().email(email).lastName("John").firstName("Smith").password("password").build();

        //Act
        userRepository.save(user);
        User userByEmail = userRepository.findByEmail(email).orElseThrow();

        //Assert
        assertThat(userByEmail).isNotNull();
        assertThat(userByEmail.getEmail()).isEqualTo(email);
    }

    @Test
    public void UserRepositoryTest_searchByQueryString_ReturnsListOfUser() {
        //Arrange and act

        User johnDoe = userRepository.save(
                User.builder().email("test1@test.com").firstName("John").lastName("Doe").password("password").build()
        );
        User janeDoe = userRepository.save(
                User.builder().email("test2@test.com").firstName("Jane").lastName("Doe").password("password").build()
        );
        User johnSmith = userRepository.save(
                User.builder().email("test3@test.com").firstName("John").lastName("Smith").password("password").build()
        );
        User janeSmith = userRepository.save(
                User.builder().email("test4@test.com").firstName("Jane").lastName("Smith").password("password").build()
        );


        String query1 = "Smith John";
        List<User> search1 = userRepository.searchByQueryString(query1);
        assertThat(search1).contains(johnSmith);

        String query2 = "JOHNN";
        List<User> search2 = userRepository.searchByQueryString(query2);
        assertThat(search2).contains(johnSmith, johnDoe);


        String query3 = "Doe Jane";
        List<User> search3 = userRepository.searchByQueryString(query3);
        assertThat(search3).contains(janeDoe);


        String query4 = "SMITH JANE";
        List<User> search4 = userRepository.searchByQueryString(query4);
        assertThat(search4).contains(janeSmith);


        String query5 = "";
        List<User> search5 = userRepository.searchByQueryString(query5);
        assertThat(search5).size().isEqualTo(0);
    }
}