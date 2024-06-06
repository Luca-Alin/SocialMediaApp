package socialmediaapp.auth.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class LoginRequest {
    private String email;
    private String password;

}