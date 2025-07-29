package GyGArquitectos.contactMethod.domain;


import GyGArquitectos.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ContactMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContactCategory category;

    @Enumerated(EnumType.STRING)
    private ContactPlatform platform;

    private String value;

    @ManyToOne
    @JoinColumn(name = "architect_id", nullable = false)
    private User architect;
}