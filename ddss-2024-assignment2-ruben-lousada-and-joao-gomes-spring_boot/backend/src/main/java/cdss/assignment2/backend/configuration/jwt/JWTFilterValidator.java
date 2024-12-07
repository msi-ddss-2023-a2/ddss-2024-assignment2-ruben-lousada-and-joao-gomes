package cdss.assignment2.backend.configuration.jwt;

import cdss.assignment2.backend.model.Account;
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
    private final String secret = System.getProperty("hashing-secret");

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

        Account account = this.accountService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(account.getUsername(),null, new ArrayList<>());
    }
}