package springboottemplate.data_services.post.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Embeddable
public class PostImage {
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;
}