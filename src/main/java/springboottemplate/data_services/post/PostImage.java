package springboottemplate.data_services.post;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Embeddable
public class PostImage {
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;
}
