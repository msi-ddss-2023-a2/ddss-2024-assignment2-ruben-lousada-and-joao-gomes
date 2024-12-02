package cdss.assignment2.backend.dto;

import lombok.Data;

@Data
public class AccountLoginRequest {

    private String username;

    private String password;
}
