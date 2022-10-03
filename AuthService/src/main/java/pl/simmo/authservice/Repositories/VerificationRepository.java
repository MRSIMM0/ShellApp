package pl.simmo.authservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.simmo.authservice.Models.UserModel;
import pl.simmo.authservice.Models.VerificationToken;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken,Long> {
        Optional<VerificationToken> findByToken(String token);

        Optional<VerificationToken> findByUser(UserModel userModel);
}
