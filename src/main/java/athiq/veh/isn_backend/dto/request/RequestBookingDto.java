package athiq.veh.isn_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestBookingDto {

    private Long customerId;  // Assuming the customer is required to make a booking
    private String destination;
    private String pickUpLocation;
    private LocalDateTime bookingDate;
    private double price;
    private String phone;
    private String address;
    private String nic;
    private int days; // Number of days for the booking

    private Long driverId;  // Optional field to set the driver for the booking

}