package GyGArquitectos.user.domain;

import GyGArquitectos.appointment.domain.Appointment;
import GyGArquitectos.contactMethod.domain.ContactMethod;
import GyGArquitectos.message.domain.Message;
import GyGArquitectos.project.domain.Project;
import GyGArquitectos.review.domain.Review;
import GyGArquitectos.userProfile.domain.UserProfile;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotNull
    private String profileImageUrl = "https://exploreconnect-bucket.s3.us-east-2.amazonaws.com/default-profile-image.png";

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "architect", cascade = CascadeType.ALL)
    private List<ContactMethod> contactMethods;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> messagesReceived;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Review> reviewsEscritas;

    @OneToMany(mappedBy = "architect", cascade = CascadeType.ALL)
    private List<Review> reviewsRecibidas;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Appointment> appointmentsHechas;

    @OneToMany(mappedBy = "architect", cascade = CascadeType.ALL)
    private List<Appointment> appointmentsRecibidas;

    @OneToMany(mappedBy = "architect", cascade = CascadeType.ALL)
    private List<Project> projects;

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}