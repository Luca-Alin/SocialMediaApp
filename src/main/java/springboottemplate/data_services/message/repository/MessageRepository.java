package springboottemplate.data_services.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springboottemplate.data_services.message.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("""
        select m
        from Message m
        where (m.senderId = ?1 and m.receiverId = ?2)
        or (m.senderId = ?2 and m.receiverId = ?1)
    """)
    List<Message> findConversationBetweenTwoUsers(String user1Id, String user2Id);
}
