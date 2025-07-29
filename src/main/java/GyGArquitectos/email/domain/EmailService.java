package GyGArquitectos.email.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    @Async
    public void correoSingIn(String to, String name) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);

        String process = templateEngine.process("sing-un-template.html", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setText(process, true);
        helper.setSubject("Te damos la Bienvenida en G&G Arquitectos!");

        mailSender.send(message);
    }

}