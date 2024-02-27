package springboottemplate.config.security;

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
import springboottemplate.config.JwtAuthenticationFilter;

import java.util.Enumeration;
import java.util.Iterator;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    public String springProfile = "development";
    private final String[] AUTH_WHITELIST = (springProfile.equals("production")) ?
            new String[]{
                    "/api/v1/*/public/**", "/**", "/api/v1/auth/**",
                    "/v3/api-docs/**", "/v3/api-docs.yaml",
                    "/swagger-ui/**", "/swagger-ui.html",
            } : new String[]{
//            "/api/v1/*/public/**",
            "/api/v1/auth/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(AUTH_WHITELIST)
                                .permitAll()

                                .anyRequest()
                                .authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(authenticationEntryPoint()))
//                .cors(AbstractHttpConfigurer::disable)
                .build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {


            StringBuilder sb = new StringBuilder();
            Enumeration<String> e = request.getHeaderNames();
            Iterator<String> it = e.asIterator();
            while (it.hasNext()) {
                String header = it.next();
                String headerContent = request.getHeader(header);
                sb.append(header)
                        .append(" ")
                        .append(headerContent)
                        .append("\n");
            }
            System.out.println(sb);

            Enumeration<String> authorizationHeaders = request.getHeaders("authorization");
            Iterator<String> authIterator = authorizationHeaders.asIterator();
            if (!authIterator.hasNext()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized: No JWT token found in request headers\"}");
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized: No valid JWT token found in request headers\"}");
            }
        };
    }
}