package GyGArquitectos.auth.utils;

import GyGArquitectos.user.domain.Role;
import GyGArquitectos.user.domain.User;
import GyGArquitectos.user.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtils {

    private final UserService userService;

    @Autowired
    public AuthorizationUtils(@Lazy UserService userService) {
        this.userService = userService;
    }

    public boolean isArchitect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.findByEmail(username);
        return user.getRole().equals(Role.ARCHITECT);
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.findByEmail(username);
        return user.getRole().equals(Role.ADMIN);
    }

    public boolean isAdminOrResourceArchitect(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.findByEmail(username);
        return user.getId().equals(id) || user.getRole().equals(Role.ARCHITECT);
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } catch (ClassCastException e) {
            return null;
        }
    }
}
