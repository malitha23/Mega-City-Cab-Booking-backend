package athiq.veh.isn_backend.dto.auth;

public record PasswordResetRequestDTO (String token, String newPassword) {

}