package springboottemplate.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import springboottemplate.data_services.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApplicationConfigTest {

    @InjectMocks
    private ApplicationConfig applicationConfig;

    /** @noinspection resource*/
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void userDetailsService_returnsUserDetailsService() {
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        assertNotNull(userDetailsService);
    }

    @Test
    void authenticationProvider_returnsAuthenticationProvider() {
        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();
        assertNotNull(authenticationProvider);
    }

    @Mock
    private AuthenticationConfiguration configuration;

    @Test
    void authenticationManager_returnsAuthenticationManager() throws Exception {
        when(configuration.getAuthenticationManager()).thenReturn(mock(AuthenticationManager.class));
        AuthenticationManager authenticationManager = applicationConfig.authenticationManager(configuration);
        assertNotNull(authenticationManager);
    }

    @Test
    void passwordEncoder_returnsPasswordEncoder() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
}