package cdss.assignment2.backend.services;

import cdss.assignment2.backend.dto.AccountCreationRequest;
import cdss.assignment2.backend.model.Account;
import cdss.assignment2.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public Account loadUserByUsername(String username) {
        Optional<Account> account = this.userRepository.findByUsername(username);

        if (account.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }

        return account.get();
    }

    public Account signup(AccountCreationRequest request) {
        Optional<Account> accountByEmail = this.userRepository.findByUsername(request.getUsername());
        if (accountByEmail.isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        String encodedPass = this.passwordEncoder.encode(request.getPassword());

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setAge(request.getAge());
        account.setPassword(encodedPass);

        return this.userRepository.save(account);
    }
}
