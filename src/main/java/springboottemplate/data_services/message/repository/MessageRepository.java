package springboottemplate.data_services.message.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.user.model.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("""
        select m
        from Message m
        where m.sender = ?1 or m.receiver = ?1
    """)
    List<Message> findMessagesSendOrReceivedByUser(User user);

    @Transactional
    @Modifying
    @Query("""
        update Message m
        set m.messageWasRead = true
        where m.receiver = ?1 and m.sender = ?2
    """)
    void setMessagesRead(User receiver, User sender);
}
