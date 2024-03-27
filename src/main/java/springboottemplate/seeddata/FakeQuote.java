package springboottemplate.seeddata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FakeQuote {
    private Integer id;
    private String quote;
    private String author;
}
