package athiq.veh.isn_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)  // If you want to include equality checks for parent class fields.
public class Bookings extends AbstractAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY) // Assuming the user is related to booking.
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private User customer;  // Assuming User is another entity class

    private String destination;

    private String pickUpLocation;

    private LocalDateTime bookingDate;

    private double price;

    private double totalAmount;

    private String phone;

    private String address;

    private String nic;

    private int days; // New field for the number of days

    private double totalPrice; // New field for the total price

    @ManyToOne(fetch = FetchType.LAZY) // Assuming the role is related to booking.
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = true) // Nullable is true to allow null values
    private User driver;  // Assuming User is related to driver, you can use a separate Driver class if needed.

}
