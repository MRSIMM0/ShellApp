package com.example.apigateway.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role {

    private Long id;
    private RoleEnum name;

    public Role(RoleEnum roleEnum) {
        this.name = roleEnum;
    }
}
