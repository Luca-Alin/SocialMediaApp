package springboottemplate.data_services.user.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springboottemplate.data_services.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnSavedUsers() {
        //Arrange
        User user = User
                .builder()
                .email("john_smith@test.com")
                .lastName("John")
                .firstName("Smith")
                .password("password")
                .build();
        //Act
        User savedUser = userRepository.save(user);

        //Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThanOneUser() {
        //Arrange
        User user = User
                .builder()
                .email("john_smith@test.com")
                .lastName("John")
                .firstName("Smith")
                .password("password")
                .build();
        User user2 = User
                .builder()
                .email("john_doe@test.com")
                .lastName("John")
                .firstName("Doe")
                .password("password")
                .build();

        //Act
        userRepository.save(user);
        userRepository.save(user2);
        List<User> userList = userRepository.findAll();

        //Assert
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUser() {
        //Arrange
        User user = User
                .builder()
                .email("john_smith@test.com")
                .lastName("John")
                .firstName("Smith")
                .password("password")
                .build();

        //Act
        User savedUser = userRepository.save(user);

        User userById = userRepository.findById(savedUser.getId()).get();

        //Assert
        Assertions.assertThat(userById).isNotNull();
    }

    @Test
    public void UserRepository_DeleteById_DeleteUser() {
        //Arrange
        User user = User
                .builder()
                .email("john_smith@test.com")
                .lastName("John")
                .firstName("Smith")
                .password("password")
                .build();

        //Act
        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());

        List<User> userList = userRepository.findAll();

        //Assert
        Assertions.assertThat(userList.size()).isEqualTo(0);
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUserByEmail() {
        //Arrange
        String email = "john_smith@test.com";
        User user = User
                .builder()
                .email(email)
                .lastName("John")
                .firstName("Smith")
                .password("password")
                .build();

        //Act
        userRepository.save(user);
        User userByEmail = userRepository.findByEmail(email).get();

        //Assert
        Assertions.assertThat(userByEmail).isNotNull();
    }

    @Test
    public void UserRepository_FindByFirstNameOrLastName_ReturnUser() {
        //Arrange
        String email = "john_smith@test.com";
        User user = User
                .builder()
                .email(email)
                .lastName("John")
                .firstName("Smith")
                .password("password")
                .build();

        String query1 = "John Smith";
        List<String> queryList1 = new ArrayList<>();
        Arrays.stream(query1.split(" ")).forEach((t) -> {
            queryList1.add(t);
            queryList1.add(t.toLowerCase(Locale.ROOT));
            queryList1.add(t.toUpperCase());
            queryList1.add(t.toUpperCase().charAt(0) + t.toLowerCase().substring(1));
        });
        String query2 = "john";
        List<String> queryList2 = new ArrayList<>();
        Arrays.stream(query2.split(" ")).forEach((t) -> {
            queryList2.add(t);
            queryList2.add(t.toLowerCase(Locale.ROOT));
            queryList2.add(t.toUpperCase());
            queryList2.add(t.toUpperCase().charAt(0) + t.toLowerCase().substring(1));
        });
        String query3 = "smith";
        List<String> queryList3 = new ArrayList<>();
        Arrays.stream(query3.split(" ")).forEach((t) -> {
            queryList3.add(t);
            queryList3.add(t.toLowerCase(Locale.ROOT));
            queryList3.add(t.toUpperCase());
            queryList3.add(t.toUpperCase().charAt(0) + t.toLowerCase().substring(1));
        });
        //Act
        userRepository.save(user);
        List<User> userBySearch1 = userRepository.searchByFirstNameInOrLastNameIn(queryList1, queryList1);
        List<User> userBySearch2 = userRepository.searchByFirstNameInOrLastNameIn(queryList2, queryList2);
        List<User> userBySearch3 = userRepository.searchByFirstNameInOrLastNameIn(queryList3, queryList3);

        //Assert
        Assertions.assertThat(userBySearch1.stream().map(u -> user.getEmail())).contains(email);
        Assertions.assertThat(userBySearch2.stream().map(u -> user.getEmail())).contains(email);
        Assertions.assertThat(userBySearch3.stream().map(u -> user.getEmail())).contains(email);

    }

}