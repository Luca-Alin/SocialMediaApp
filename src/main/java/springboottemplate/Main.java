package springboottemplate;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(title = "Library APIS", version = "1.0", description = "Library Management Apis.")
)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
