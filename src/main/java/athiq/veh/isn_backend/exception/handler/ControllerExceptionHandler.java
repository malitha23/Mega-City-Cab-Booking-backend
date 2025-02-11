package athiq.veh.isn_backend.exception.handler;

import athiq.veh.isn_backend.constant.ResponseStatusEnum;
import athiq.veh.isn_backend.dto.response.ErrorResponseDTO;
import athiq.veh.isn_backend.exception.BadRequestException;
import athiq.veh.isn_backend.exception.OtpAuthenticationFailedException;
import athiq.veh.isn_backend.exception.ResourceNotFoundException;
import athiq.veh.isn_backend.exception.UserNotActivatedException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> resourceNotFoundExceptionHandler(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponseDTO message = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ResponseStatusEnum.RESOURCE_NOT_FOUND,
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> globalExceptionHandler(Exception ex, WebRequest request) {

        logger.error("server error", ex);

        ErrorResponseDTO message = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ResponseStatusEnum.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> usernameNotFoundExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponseDTO message = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                ResponseStatusEnum.INVALID_CREDENTIALS,
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OtpAuthenticationFailedException.class)
    public ResponseEntity<ErrorResponseDTO> otpAuthenticationFailedExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponseDTO message = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                ResponseStatusEnum.AUTHENTICATION_FAILED,
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotActivatedException.class)
    public ResponseEntity<ErrorResponseDTO> userNotActivatedExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponseDTO message = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                ResponseStatusEnum.USER_NOT_ACTIVATED,
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> badRequestExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponseDTO message = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ResponseStatusEnum.BAD_REQUEST,
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
