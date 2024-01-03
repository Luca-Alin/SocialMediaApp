package springboottemplate.data_services.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboottemplate.data_services.post.Post;
import springboottemplate.data_services.user.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserIn(List<User> users);
    List<Post> findAllByUser(User user);
}
