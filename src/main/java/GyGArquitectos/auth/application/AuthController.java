package GyGArquitectos.auth.application;

import GyGArquitectos.auth.domain.AuthService;
import GyGArquitectos.auth.domain.GoogleAuthService;
import GyGArquitectos.auth.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authenticationService;
    private final GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<GoogleAuthResponseDto> validateGoogleAuthToken(@RequestBody GoogleAuthRequestDto googleAuthRequestDto) {
        return ResponseEntity.ok(googleAuthService.validate(googleAuthRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> login(@RequestBody LoginDto logInDTO) {
        return ResponseEntity.ok(authenticationService.login(logInDTO));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponseDto> signin(@RequestBody SignUpDto signUpDTO) {
        return ResponseEntity.ok(authenticationService.signUp(signUpDTO));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
        return ResponseEntity.ok("Logged out successfully");
    }
}