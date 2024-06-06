package socialmediaapp.data_services.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import socialmediaapp.data_services.user.model.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query("""
            select u
            from User u
            where lower(?1) like concat(lower(u.firstName), '%')
            or lower(?1) like concat(lower(u.lastName), '%')
            or lower(u.firstName) like lower(?1)
            or lower(u.lastName) like lower(?1)
            or lower(concat(u.firstName, ' ', u.lastName)) = lower(?1)
            or lower(concat(u.lastName, ' ', u.firstName)) = lower(?1)
            order by
                   case
                      when lower(concat(u.firstName, ' ', u.lastName)) = lower(?1) then 1
                      when lower(concat(u.lastName, ' ', u.firstName)) = lower(?1) then 2
                      when lower(u.firstName) like lower(?1) then 3
                      when lower(u.lastName) like lower(?1) then 4
                      when lower(?1) like concat(lower(u.firstName), '%') then 5
                      when lower(?1) like concat(lower(u.lastName), '%') then 6
                      else 7
                   end
            """)
    List<User> searchByQueryString(String query);


    boolean existsByEmail(String email);
}
