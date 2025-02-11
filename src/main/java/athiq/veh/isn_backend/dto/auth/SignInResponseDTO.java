package athiq.veh.isn_backend.dto.auth;

import org.springframework.security.core.GrantedAuthority;

public record SignInResponseDTO(
        String accessToken,
        UserMinDTO user,
        java.util.Collection<? extends GrantedAuthority> authorities
) {
}
