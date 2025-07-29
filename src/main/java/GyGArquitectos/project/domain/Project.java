package GyGArquitectos.project.domain;

import GyGArquitectos.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "architect_id", nullable = false)
    private User architect;
}