package athiq.veh.isn_backend.dto.response;

public record UserDetailsResponseDTO(
        Long id,
        String firstName,
        String LastName,
        String email,
        String imageUrl
) {
}
