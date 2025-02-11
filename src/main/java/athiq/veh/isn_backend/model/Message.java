
package athiq.veh.isn_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore // Ignore the actual User entity during serialization
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore // Ignore the actual User entity during serialization
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    private String content;
    private LocalDateTime timestamp;

    private boolean isRead = false;

    @Column(name = "post_id", nullable = true)
    private Long postId; // Nullable postId

    @Column(name = "item_id", nullable = true)
    private Long itemId; // Nullable itemId

    @JsonProperty("senderId")
    public Long getSenderId() {
        return sender != null ? sender.getId() : null;
    }

    @JsonProperty("receiverId")
    public Long getReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }

}

