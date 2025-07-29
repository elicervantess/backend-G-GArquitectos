package GyGArquitectos.events.SignUp;

import GyGArquitectos.email.domain.EmailService;
import GyGArquitectos.events.SignUp.SignUpEvent;
import jakarta.mail.MessagingException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Component
public class SignUpEventListener {

    private final EmailService emailService;

    public SignUpEventListener(EmailService emailService) {
        this.emailService = emailService;
    }
    @EventListener
    @Async
    public void handleHelloEmailEvent(SignUpEvent event) throws MessagingException {
        emailService.correoSingIn(event.getEmail(), event.getUser());
    }
}