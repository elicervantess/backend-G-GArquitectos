package GyGArquitectos.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import GyGArquitectos.user.domain.Role;
import GyGArquitectos.user.domain.User;
import GyGArquitectos.user.domain.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private final UserService userService;
    @Autowired
    public JwtService(UserService userService) {
        this.userService = userService;
    }

    private Set<String> invalidatedTokens = new HashSet<>();

    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    public String extractUserEmail(String token) {return JWT.decode(token).getSubject();}





    public List<Role> extractRoles(String token) {
        String roleString = JWT.decode(token).getClaim("role").asString();
        return List.of(Role.valueOf(roleString));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    public String generateToken(UserDetails data) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 horas de validez

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String role = data.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(Role.GUEST.name());

        return JWT.create()
                .withSubject(data.getUsername())
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    public String generateToken(User user) {
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.getUsername());
        return generateToken(userDetails);
    }

    public void validateToken(String token, String userEmail) throws AuthenticationException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            verifier.verify(token);

            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

            List<Role> roles = extractRoles(token);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    token,
                    authorities

            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);

        } catch (JWTVerificationException ex) {
            throw new AuthenticationException("Token JWT no válido", ex) {};
        }
    }
}