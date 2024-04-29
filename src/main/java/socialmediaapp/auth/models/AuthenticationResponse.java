package springboottemplate.auth.models;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public final class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
}
