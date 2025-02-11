package athiq.veh.isn_backend.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

public class RandomPasswordUtil {

    public static String generateOTP(Integer length) {
        try {
            // Convert timestamp to string and get bytes
            String timestampString = String.valueOf(LocalDateTime.now());
            byte[] timestampBytes = timestampString.getBytes(StandardCharsets.UTF_8);

            // Hash the timestamp bytes using SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(timestampBytes);

            byte[] first4Bytes = new byte[4];
            System.arraycopy(hashBytes, 0, first4Bytes, 0, 4);
            String base64Code = Base64.getUrlEncoder().withoutPadding().encodeToString(first4Bytes);

            // Return the first 6 characters of the encoded string
            return base64Code.substring(0, length);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating code", e);
        }
    }

}
