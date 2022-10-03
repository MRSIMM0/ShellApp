package pl.simmo.authservice.Mailing;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.simmo.authservice.Models.UserModel;
import pl.simmo.authservice.Utils.EnvUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailingService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    private final  EnvUtil util;

    @SneakyThrows
    public void sendEmail(UserModel userModel, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();

        String mailAddress = util.getServerUrlPrefix()+ "/api/v1/auth/activate/"+token;


        context.setVariables(Map.of("userModel",userModel,"mailAddress",mailAddress));

        String html = templateEngine.process("activate", context);
        helper.setTo(userModel.getEmail());
        helper.setText(html, true);
        helper.setSubject("Activate Account");
        javaMailSender.send(message);

    }
}
