package athiq.veh.isn_backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseBookingDto {

    private Long bookingId;
    private Long customerId;  // Customer ID to identify the user
    private String destination;
    private String pickUpLocation;
    private LocalDateTime bookingDate;
    private double price;
    private double totalAmount;  // Total amount after taxes
    private String phone;
    private String address;
    private String nic;
    private int days;
    private double totalPrice;  // Total price before taxes

    private Long driverId;  // Optional driver ID for the booking (may or may not be assigned)

}