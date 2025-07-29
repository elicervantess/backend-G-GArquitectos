package GyGArquitectos.userProfile.domain;

import GyGArquitectos.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoUrl;
    private String fullName;
    private String bio;
    private String instagram;
    private String linkedin;
    private String whatsapp;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}