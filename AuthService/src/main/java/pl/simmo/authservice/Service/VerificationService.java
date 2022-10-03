package pl.simmo.authservice.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.simmo.authservice.Exceptions.TokenNotFoundException;
import pl.simmo.authservice.Mailing.MailingService;
import pl.simmo.authservice.Models.UserModel;
import pl.simmo.authservice.Models.VerificationToken;
import pl.simmo.authservice.Repositories.VerificationRepository;
import pl.simmo.authservice.Security.TokenManager;
import pl.simmo.authservice.Security.VerificationManager;

import javax.mail.MessagingException;
import java.util.Date;
@Service
@RequiredArgsConstructor
public class VerificationService {
    private final VerificationRepository verificationRepository;

    private final VerificationManager verificationManager;

    private final MailingService mailingService;

    public void saveToken(UserModel userModel){

        if(verificationRepository.findByUser(userModel).isPresent()){
            verificationRepository.delete( verificationRepository.findByUser(userModel).get());
        }


        String token = verificationManager.generateToken(userModel);


        verificationRepository.save(new VerificationToken(token,userModel,new Date()));
        try {
            mailingService.sendEmail(userModel,token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public VerificationToken findByToken(String token) throws TokenNotFoundException {
        if(!verificationRepository.findByToken(token).isPresent()) throw new TokenNotFoundException();
        return verificationRepository.findByToken(token).get();
    }

    public void delete(VerificationToken verificationToken){
        verificationRepository.delete(verificationToken);
    }


}
