package athiq.veh.isn_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@EqualsAndHashCode(callSuper = true)
@EqualsAndHashCode(callSuper = true, exclude = "user")
@Entity
@Data
public class FileBlob extends AbstractAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    @JoinColumn(name = "post_id", nullable = false)
//    private Post post;

    @JsonIgnore
    @OneToOne(mappedBy = "imageBlob")
    private User user;

}
