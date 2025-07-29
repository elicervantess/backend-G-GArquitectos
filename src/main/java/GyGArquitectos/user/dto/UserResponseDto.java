package GyGArquitectos.user.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String fullName;
    private String role;
}