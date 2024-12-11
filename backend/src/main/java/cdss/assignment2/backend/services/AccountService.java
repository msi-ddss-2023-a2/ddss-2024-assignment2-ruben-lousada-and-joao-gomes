package cdss.assignment2.backend.services;

import cdss.assignment2.backend.dto.AccountCreationRequest;
import cdss.assignment2.backend.model.Account;
import cdss.assignment2.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public Account loadUserByUsername(String email) {
        Optional<Account> account = this.userRepository.findByUsername(email);

        if (account.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }

        return account.get();
    }

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$";

    // Compile the regex pattern
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public Account signup(AccountCreationRequest request) {
        Optional<Account> accountByEmail = this.userRepository.findByUsername(request.getEmail());
        if (accountByEmail.isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        String password = request.getPassword();

        if (password.length() > 32 || !pattern.matcher(password).matches()) {
            throw new RuntimeException("Password is not fittable");
        }

        String encodedPass = this.passwordEncoder.encode(request.getPassword());

        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(encodedPass);

        return this.userRepository.save(account);
    }

    public void incrementFailedLoginAttempts(String email) {
        this.userRepository.incrementFailedLoginAttempts(email);
    }

    public void resetFailedLoginAttempts(String email) {
        this.userRepository.resetFailedLoginAttempts(email);
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        System.out.println(authentication.getPrincipal());
        return !(authentication.getPrincipal() instanceof String
                && authentication.getPrincipal().equals("anonymousUser"));
    }
}
