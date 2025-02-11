package athiq.veh.isn_backend.dto.auth;

public record OtpValidateRequestDTO(
        String email,
        String otp
) {
}
