package cdss.assignment2.backend.configuration.jwt;

import cdss.assignment2.backend.services.AccountService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import cdss.assignment2.backend.repository.UserRepository;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@PropertySource(value = {"classpath:application.properties"})
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final String secret = System.getProperty("hashing-secret");

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
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

            try {
                Authentication auth = authenticationManager.authenticate(authenticationToken);

                Account account = accountService.loadUserByUsername(accountLoginRequest.getUsername());
                if (account.getFailedLoginAttemps() > 5) {
                    throw new RuntimeException("account locked");
                }

                return auth;
            } catch(AuthenticationException e) {
                accountService.incrementFailedLoginAttempts(accountLoginRequest.getUsername());
                throw e;
            }
        } catch(IOException e) {
            System.out.println("io failed");
        }

        return authenticationManager.authenticate(null);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        Account account = (Account) authResult.getPrincipal();

        accountService.resetFailedLoginAttempts(account.getUsername());

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
