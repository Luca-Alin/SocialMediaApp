package springboottemplate.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import springboottemplate.auth.models.AuthenticationResponse;
import springboottemplate.auth.models.LoginRequest;
import springboottemplate.auth.models.RegisterRequest;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;

    private final Random random = new Random();
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        try {
            var authenticationResponse = authenticationService.register(registerRequest);
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(
            @RequestBody LoginRequest loginRequest
    ) {
        try {
            var authenticationResponse = authenticationService.login(loginRequest);
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Your credentials are wrong", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }


    @GetMapping("/random")
    public ResponseEntity<AuthenticationResponse> loginAsRandomUser() {

        List<User> users = userRepository.findAll();
        User user = users.get(random.nextInt(users.size()));
        LoginRequest loginRequest = LoginRequest
                .builder()
                .email(user.getEmail())
                .password("password")
                .build();

        var authentication = authenticationService.login(loginRequest);
        return new ResponseEntity<>(authentication, HttpStatus.OK);
    }
}
