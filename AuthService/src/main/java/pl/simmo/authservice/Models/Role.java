package pl.simmo.authservice.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "role")
@Data
@Entity
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    public Role(RoleEnum roleEnum) {
        this.name = roleEnum;
    }
}
