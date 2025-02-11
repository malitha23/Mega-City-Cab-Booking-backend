package athiq.veh.isn_backend.exception;

import java.io.Serial;

public class OtpAuthenticationFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OtpAuthenticationFailedException(String message) {
        super(message);
    }
}
