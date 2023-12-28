package springboottemplate.data.address;

import jakarta.persistence.*;
import lombok.Data;
import springboottemplate.data.employee.Employee;

@Data

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String street;

    private String houseNumber;

    private String zipCode;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}