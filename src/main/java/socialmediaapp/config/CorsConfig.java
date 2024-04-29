package springboottemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    private final List<String> allowedOrigins = List.of("*");
    private final List<String> allowedHeaders = List.of("Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept", "Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers");
    private final List<String> exposedHeaders = List.of("Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
    private final List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");

    @Bean
    public CorsFilter corsFilter() {
        var cors = new CorsConfiguration();

        cors.setAllowCredentials(true);
        cors.setAllowedOrigins(allowedOrigins);
        cors.setAllowedHeaders(allowedHeaders);
        cors.setExposedHeaders(exposedHeaders);
        cors.setAllowedMethods(allowedMethods);

        var url = new UrlBasedCorsConfigurationSource();
        url.registerCorsConfiguration("/**", cors);

        return new CorsFilter(url);
    }

}
