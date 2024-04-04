package springboottemplate.data_services.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboottemplate.data_services.group.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
