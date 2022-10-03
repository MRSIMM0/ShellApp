package com.example.apigateway.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModelDTO {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
}
