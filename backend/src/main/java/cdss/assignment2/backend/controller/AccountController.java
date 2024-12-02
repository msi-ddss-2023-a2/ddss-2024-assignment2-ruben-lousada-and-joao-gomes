package cdss.assignment2.backend.controller;

import cdss.assignment2.backend.dto.AccountCreationRequest;
import cdss.assignment2.backend.model.Account;
import cdss.assignment2.backend.repository.UserRepository;
import cdss.assignment2.backend.services.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("accounts")
@RestController
public class AccountController {

    public AccountService accountService;

    public AccountController(AccountService userRepository) {
        this.accountService = userRepository;
    }

    @PostMapping
    public Account createAccount(AccountCreationRequest accountCreationRequest) {
        return this.accountService.signup(accountCreationRequest);
    }
}
