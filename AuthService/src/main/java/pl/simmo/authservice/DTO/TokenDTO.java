package pl.simmo.authservice.DTO;

import lombok.Data;

@Data
public class TokenDTO {
    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}
