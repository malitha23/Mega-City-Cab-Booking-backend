package athiq.veh.isn_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDTO {
    private String message;
    private String expirationTime;
    private String accessToken;
    private UserMinDTO user;
    private Collection<? extends GrantedAuthority> authorities;
}
