package GyGArquitectos.user.domain;

import GyGArquitectos.exceptions.*;
import GyGArquitectos.user.dto.UserRequestDto;
import GyGArquitectos.user.dto.UserResponseDto;
import GyGArquitectos.user.infraestructure.UserRepository;
import GyGArquitectos.mediaStorage.domain.MediaStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaStorageService mediaStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean(name = "UserDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository
                    .findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return (UserDetails) user;
        };
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponseDto(user);
    }

    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToUserResponseDto(user);
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User with email " + userRequestDto.getEmail() + " already exists");
        }

        User user = new User();
        user.setFullName(userRequestDto.getFullName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(Role.CLIENT); // Rol fijo al registrarse

        userRepository.save(user);
        return mapToUserResponseDto(user);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFullName(userRequestDto.getFullName());
        user.setEmail(userRequestDto.getEmail());

        String newPassword = userRequestDto.getPassword();
        if (!passwordEncoder.matches(newPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        // No se actualiza el rol desde el cliente

        userRepository.save(user);
        return mapToUserResponseDto(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }


    private UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFullName(user.getFullName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRole(user.getRole().name());
        return userResponseDto;
    }
}