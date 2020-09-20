package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pl.edu.agh.airsystem.util.ResourceReader;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailSenderService {

    private final String username = System.getenv("EMAIL_LOGIN");
    private final String password = System.getenv("EMAIL_PASSWORD");
    private final String domain = System.getenv("DOMAIN");


    public void sendActivationString(String email, String activateString) throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        String activationLink = domain + "/activateAccount/" + email + "/" + activateString;

        ClassPathResource resource = new ClassPathResource("accountActivation/email.html");
        String messageContent = ResourceReader.asString(resource);
        messageContent = messageContent.replace("ACTIVATION_LINK", activationLink);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(email)
        );
        message.setSubject("Airella - Activate your account");
        message.setContent(messageContent, "text/html; charset=utf-8");

        Transport.send(message);
    }

}
