package springboottemplate.data_services.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
//    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] profileImage;

    @Column(length = 4000)
    private String bio;


}
