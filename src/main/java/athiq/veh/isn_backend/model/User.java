package athiq.veh.isn_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "imageBlob")
//@EqualsAndHashCode(callSuper = true)
public class User extends AbstractAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String imageUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_blob", referencedColumnName = "id")
    private FileBlob imageBlob;

    private Boolean enabled;

    private Boolean  isActivated;

    private String otp;

    private  Boolean isAdminRenewed;

    private LocalDateTime passwordExpireDate;

    private LocalDateTime OtpExpireDate;

    private String passwordResetToken;

    private LocalDateTime passwordResetTokenExpiration;

//    private boolean hasUnreadMessages;
//
//    public boolean hasUnreadMessages() {
//        return hasUnreadMessages;
//    }
//
//    public void setHasUnreadMessages(boolean hasUnreadMessages) {
//        this.hasUnreadMessages = hasUnreadMessages;
//    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
