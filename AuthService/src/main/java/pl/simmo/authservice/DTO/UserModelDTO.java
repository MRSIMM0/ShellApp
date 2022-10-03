package pl.simmo.authservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import pl.simmo.authservice.Models.Role;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserModelDTO {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
}
