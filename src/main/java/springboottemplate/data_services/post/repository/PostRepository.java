package springboottemplate.data_services.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springboottemplate.data_services.post.model.Post;
import springboottemplate.data_services.user.model.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findAllByUserOrderByCreatedAt(User user, Pageable pageable);

    @Query("""
                select p
                from Post p
                where p.user in (
                    select f.sender
                    from Friendship f
                    where (f.receiver = ?1 or f.sender = ?1) and f.friendshipStatus = 1
                ) or p.user in (
                    select f.receiver
                    from Friendship f
                    where (f.receiver = ?1 or f.sender = ?1) and f.friendshipStatus = 1
                ) or p.user = ?1
                order by p.createdAt desc
            """)
    List<Post> findAllPostsByUserAndHisFriends(User user, Pageable pageable);

}
