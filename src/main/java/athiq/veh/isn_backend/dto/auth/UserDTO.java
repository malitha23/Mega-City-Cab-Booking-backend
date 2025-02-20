package athiq.veh.isn_backend.dto.auth;

// UserDTO.java
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String imageUrl;

    public UserDTO() {}

    public UserDTO(Long id, String firstName, String lastName, String imageUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;

    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}
