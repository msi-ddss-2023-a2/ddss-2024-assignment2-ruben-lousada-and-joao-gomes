package cdss.assignment2.backend.configuration;

import cdss.assignment2.backend.configuration.jwt.JWTAuthenticationFilter;
import cdss.assignment2.backend.services.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    private final AccountService accountService;

    public SecurityConfiguration(AccountService accountService) {
        this.accountService = accountService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http
        //         .csrf().disable()
        //         //  withDefaults() uses by default a Bean by the name of corsConfigurationSource
        //         .cors(withDefaults())
        //         .authorizeHttpRequests()
        //         .requestMatchers(HttpMethod.POST, "/api/v**/accounts/register", "/login").permitAll()
        //         .requestMatchers(HttpMethod.GET, "/api/v**/**").permitAll()
        //         .anyRequest().authenticated()
        //         .and()
        //         .addFilter(new JWTAuthenticationFilter(authenticationManager()))
        //         .addFilter(new JWTFilterValidator(authenticationManager(), this.accountDetailsService))
        //         .sessionManagement()
        //         .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080/"));
        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(accountService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
