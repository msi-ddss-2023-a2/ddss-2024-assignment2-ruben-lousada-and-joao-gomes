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

import java.io.IOException;
import java.util.Date;

@PropertySource(value = {"classpath:application.properties"})
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String secret = "secret-key-that-should-come-from-environmental-value";

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        AccountLoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), AccountLoginRequest.class);
        AbstractAuthenticationToken authenticationToken;
        authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        return authenticationManager.authenticate(authenticationToken);
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
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().print(mapper.writeValueAsString("Could not authenticate"));
        response.setHeader("Content-Type", "application/json;");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}