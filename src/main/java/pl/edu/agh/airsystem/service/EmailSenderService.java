package pl.edu.agh.airsystem.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
@NoArgsConstructor
public class EmailSenderService {

    @Value("${airella.email.activation.enabled}")
    private boolean emailActivationEnabled;

    @Value("${airella.email.activation.smtp.host}")
    private String host;

    @Value("${airella.email.activation.smtp.port}")
    private String port;

    @Value("${airella.email.activation.smtp.auth}")
    private boolean authEnabled;

    @Value("${airella.email.activation.smtp.starttls.enable}")
    private boolean startTlsEnable;

    @Value("${airella.email.activation.credentials.username}")
    private String username;

    @Value("${airella.email.activation.credentials.password}")
    private String password;

    @Value("${airella.domain}")
    private String domain;


    public void sendActivationString(String email, String activateString) throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", authEnabled);
        prop.put("mail.smtp.starttls.enable", startTlsEnable);

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

    public boolean isEmailActivationEnabled() {
        return emailActivationEnabled;
    }
}
