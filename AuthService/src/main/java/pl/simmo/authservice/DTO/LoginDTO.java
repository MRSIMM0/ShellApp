package pl.simmo.authservice.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    private Long id;

    private String username;

    private String password;


}
