package athiq.veh.isn_backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import athiq.veh.isn_backend.constant.ResponseStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private Integer statusCode;

    @Enumerated(EnumType.STRING)
    private ResponseStatusEnum status;

    private LocalDateTime timeStamp;

    private String message;

    private String description;

    // Constructor
    public ErrorResponse(Integer statusCode, ResponseStatusEnum status, LocalDateTime timeStamp, String message, String description) {
        this.statusCode = statusCode;
        this.status = status;
        this.timeStamp = timeStamp;
        this.message = message;
        this.description = description;
    }
}
