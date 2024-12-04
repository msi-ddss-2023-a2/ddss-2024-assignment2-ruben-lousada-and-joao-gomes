package cdss.assignment2.backend.configuration.jwt;

import com.auth0.jwt.JWT;
import cdss.assignment2.backend.dto.AccountLoginRequest;
import cdss.assignment2.backend.model.Account;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import cdss.assignment2.backend.repository.UserRepository;

import java.io.IOException;
import java.util.Date;

@PropertySource(value = {"classpath:application.properties"})
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final String secret = "secret-key-that-should-come-from-environmental-value";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        try {
            AccountLoginRequest accountLoginRequest = new ObjectMapper().readValue(request.getInputStream(), AccountLoginRequest.class);
            AbstractAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(accountLoginRequest.getUsername(), accountLoginRequest.getPassword());
            if (userRepository.findByUsername(accountLoginRequest.getUsername()).getFailedLoginAttemps() > 5) {
                throw new RuntimeException("account locked");
            }
            return authenticationManager.authenticate(authenticationToken);
        } catch(IOException e) {
            return authenticationManager.authenticate(null);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        Account account = (Account) authResult.getPrincipal();

        String jwtToken = JWT.create().
                withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000 * 60 * 24))
                .sign(Algorithm.HMAC512(secret));

        response.getWriter().write(jwtToken);
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        AccountLoginRequest accountLoginRequest = new ObjectMapper().readValue(request.getInputStream(), AccountLoginRequest.class);
        userRepository.incrementFailedLoginAttempts(accountLoginRequest.getUsername());

        
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().print(mapper.writeValueAsString("Could not authenticate"));
        response.setHeader("Content-Type", "application/json;");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
