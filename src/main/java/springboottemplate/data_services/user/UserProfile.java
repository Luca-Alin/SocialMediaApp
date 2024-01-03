package springboottemplate.data_services.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class UserProfile {
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] profileImage;
    private String bio;
}
