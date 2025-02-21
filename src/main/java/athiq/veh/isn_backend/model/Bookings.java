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

    private String phone;

    private String address;

    private String nic;

    private int days; // New field for the number of days

    private double totalPrice; // New field for the total price

    private int status; // Default value is false

    @Column(name = "driver_id", nullable = true)
    private Long driverId;

    // Assuming you have a Vehicle entity that represents the vehicle information.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Item item;  // The vehicle entity associated with the booking

    // If you just want to store the vehicleId as a long (without associating it with a Vehicle entity):
    // private Long vehicleId;
}
