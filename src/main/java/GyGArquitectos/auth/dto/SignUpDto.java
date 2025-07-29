package GyGArquitectos.auth.dto;



import GyGArquitectos.user.domain.Role;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class SignUpDto {

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private Role role;

}