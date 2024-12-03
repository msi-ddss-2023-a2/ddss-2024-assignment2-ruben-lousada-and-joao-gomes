package cdss.assignment2.backend.dto;

import cdss.assignment2.backend.model.Account;
import lombok.Data;
import lombok.Getter;

public class AccountCreationRequest {
    private String email;

    private String password;

    private Short age;

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
