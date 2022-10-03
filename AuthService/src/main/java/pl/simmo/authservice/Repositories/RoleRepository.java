package pl.simmo.authservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.simmo.authservice.Models.Role;
import pl.simmo.authservice.Models.RoleEnum;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM role r WHERE r.name= ?1")
    Optional<Role> findByName(String name);
}
