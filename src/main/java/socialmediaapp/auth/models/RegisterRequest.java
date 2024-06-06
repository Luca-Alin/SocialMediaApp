package socialmediaapp.auth.models;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public final class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}