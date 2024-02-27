package springboottemplate.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboottemplate.auth.models.AuthenticationResponse;
import springboottemplate.auth.models.LoginRequest;
import springboottemplate.auth.models.RegisterRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class
AuthController {
    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        var authenticationResponse = authenticationService.register(registerRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(
            @RequestBody LoginRequest loginRequest
    ) {
        var authenticationResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }


    @GetMapping("/random")
    public ResponseEntity<Object> loginAsRandomUser() {
        return null;
    }
}
