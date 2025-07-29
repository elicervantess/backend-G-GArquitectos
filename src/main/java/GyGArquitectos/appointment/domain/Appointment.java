package GyGArquitectos.appointment.domain;

import GyGArquitectos.user.domain.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datetime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private String message;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "architect_id", nullable = false)
    private User architect;
}