package GyGArquitectos.auth.domain;

import GyGArquitectos.auth.dto.JwtAuthResponseDto;
import GyGArquitectos.auth.dto.LoginDto;
import GyGArquitectos.auth.dto.SignUpDto;
import GyGArquitectos.config.JwtService;
import GyGArquitectos.events.SignUp.SignUpEvent;
import GyGArquitectos.exceptions.UserAlreadyExistException;
import GyGArquitectos.user.domain.Role;
import GyGArquitectos.user.domain.User;
import GyGArquitectos.user.infraestructure.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public JwtAuthResponseDto login(LoginDto logInDto) {
        User user = userRepository.findByEmail(logInDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        if (!passwordEncoder.matches(logInDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        JwtAuthResponseDto response = new JwtAuthResponseDto();
        response.setToken(jwtService.generateToken(user));
        return response;
    }

    public JwtAuthResponseDto signUp(SignUpDto signupDto) {
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Email already exist");
        }

        applicationEventPublisher.publishEvent(new SignUpEvent(signupDto.getEmail(), signupDto.getName()));

        User user = new User();
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setFullName(signupDto.getName());
        user.setRole(signupDto.getRole());

        userRepository.save(user);

        userRepository.save(user);
        JwtAuthResponseDto response = new JwtAuthResponseDto();
        response.setToken(jwtService.generateToken(user));
        return response;
    }

    public boolean verifyPasswordByEmail(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtService.resolveToken(request);
        if (token != null) {
            jwtService.invalidateToken(token);
        }
    }

}