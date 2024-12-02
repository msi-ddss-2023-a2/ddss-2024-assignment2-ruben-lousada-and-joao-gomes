package cdss.assignment2.backend.dto;

import cdss.assignment2.backend.model.Account;
import lombok.Data;

@Data
public class AccountCreationRequest {
    private String username;

    private String email;

    private String password;

    private Short age;

    public Account getUser() {
        return new Account();
    }
}
