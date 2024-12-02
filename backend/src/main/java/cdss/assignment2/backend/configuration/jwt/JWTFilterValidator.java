package cdss.assignment2.backend.configuration.jwt;

import cdss.assignment2.backend.services.AccountService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@PropertySource(value = {"classpath:application.properties"})
public class JWTFilterValidator extends BasicAuthenticationFilter {

    /*
        TODO: Make secret use env variable
     */
    private String secret = "secret-key-that-should-come-from-environmental-value";

    private final AccountService accountService;

    public JWTFilterValidator(AuthenticationManager authenticationManager, AccountService accountService) {
        super(authenticationManager);
        this.accountService = accountService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (authorizationHeader == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace("Bearer ", "");
        UsernamePasswordAuthenticationToken authenticationToken = this.getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        String username = JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token)
                .getSubject();

        if (!this.accountService.checkUserExistence(username)) {
            throw new RuntimeException("JWT content is not valid " + username);
        }

        return new UsernamePasswordAuthenticationToken(username,null, new ArrayList<>());
    }
}