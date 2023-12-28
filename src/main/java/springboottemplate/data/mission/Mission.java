package springboottemplate.data.mission;

import jakarta.persistence.*;
import lombok.Data;
import springboottemplate.data.employee.Employee;

import java.util.List;

@Data

@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer duration;


    @ManyToMany(mappedBy = "missions")
    private List<Employee> employees;
}
