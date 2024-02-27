package springboottemplate.auth.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class LoginRequest {
    private String email;
    private String password;

}