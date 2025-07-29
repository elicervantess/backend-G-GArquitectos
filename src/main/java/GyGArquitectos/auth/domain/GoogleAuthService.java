package GyGArquitectos.auth.domain;

import com.google.auth.oauth2.TokenVerifier;
import GyGArquitectos.auth.dto.GoogleAuthRequestDto;
import GyGArquitectos.auth.dto.GoogleAuthResponseDto;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthService {
    public GoogleAuthResponseDto validate(GoogleAuthRequestDto googleAuthRequestDto) {
        TokenVerifier tokenVerifier = TokenVerifier.newBuilder().build();

        try {
            tokenVerifier.verify(googleAuthRequestDto.getToken());
        } catch (TokenVerifier.VerificationException e) {
            return new GoogleAuthResponseDto(false);
        }

        return new GoogleAuthResponseDto(true);
    }
}