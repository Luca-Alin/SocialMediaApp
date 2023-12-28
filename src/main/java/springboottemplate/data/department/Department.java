package springboottemplate.data.department;

import jakarta.persistence.*;
import lombok.Data;
import springboottemplate.data.employee.Employee;

import java.util.List;

@Data

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
