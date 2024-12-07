package cdss.assignment2.backend.dto;

import cdss.assignment2.backend.model.Account;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;

public class AccountCreationRequest {
    @Email
    private String email;

    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
