package socialmediaapp.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import socialmediaapp.config.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/api/v1/*/public/**",
            "/chat"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(AUTH_WHITELIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                        .and()
                        .addFilterBefore(new OncePerRequestFilter() {
                            @Override
                            protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) {
                                try {
                                    Thread.sleep(1750);
                                    fc.doFilter(req, res);
                                } catch (Exception ignored) {
                                }
                            }
                        }, BasicAuthenticationFilter.class)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling(configurer -> configurer.authenticationEntryPoint(authenticationEntryPoint()))
                .build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (_, response, authException) -> {
            String sb = "Error: " + "\n" +
                        "Cause: " + authException.getCause() + "\n" +
                        "Message: " + authException.getMessage() + "\n";

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(sb);

        };
    }
}