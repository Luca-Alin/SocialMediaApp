package springboottemplate.data_services.message.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboottemplate.data_services.message.Message;
import springboottemplate.data_services.user.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllBySendingUserOrReceivingUser(User userA1, User userA2);
}
