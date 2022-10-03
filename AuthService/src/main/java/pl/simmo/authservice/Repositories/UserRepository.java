package pl.simmo.authservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.simmo.authservice.Models.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {
    @Query(nativeQuery = true,value = "SELECT * FROM usermodel u WHERE u.username = :username")
    Optional<UserModel> findByUsername(@Param("username") String username);

    Optional<UserModel> findByEmail(String email);

}
