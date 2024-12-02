package cdss.assignment2.backend.controller;

import cdss.assignment2.backend.dto.AccountCreationRequest;
import cdss.assignment2.backend.model.Account;
import cdss.assignment2.backend.repository.UserRepository;
import org.springframework.stereotype.Controller;

@Controller
public class AccountController {

    public UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Account createUser(AccountCreationRequest accountCreationRequest) {
        this.userRepository.save();
    }
}
