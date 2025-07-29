package GyGArquitectos.user.dto;

import lombok.Data;
@Data
public class UserRequestDto {
    // For registration
    private String email;
    private String password;
    private String fullName;
}
