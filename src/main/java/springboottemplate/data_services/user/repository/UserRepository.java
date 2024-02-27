package springboottemplate.data_services.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboottemplate.data_services.user.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> searchByFirstNameInOrLastNameIn(List<String> list1, List<String> list2);
}
