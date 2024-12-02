package cdss.assignment2.backend.dto;

import cdss.assignment2.backend.model.Account;
import lombok.Data;
import lombok.Getter;

public class AccountCreationRequest {
    private String username;

    private String email;

    private String password;

    private Short age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }
}
