package GyGArquitectos.events.SignUp;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignUpEvent extends ApplicationEvent {
    private final String email;
    private final String user;

    public SignUpEvent(String email, String user) {
        super(email);
        this.email = email;
        this.user = user;
    }
}