package GyGArquitectos.auth.dto;

import lombok.Data;

@Data
public class JwtAuthResponseDto {
    private String token;
    private String message;
}