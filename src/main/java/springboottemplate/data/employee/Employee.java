package springboottemplate.data.employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboottemplate.data.address.Address;
import springboottemplate.data.department.Department;
import springboottemplate.data.mission.Mission;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


    @ManyToMany
    @JoinTable(
        name = "employee_mission",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "mission_id")
    )
    private List<Mission> missions;
}