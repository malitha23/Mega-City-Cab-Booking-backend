package athiq.veh.isn_backend.dto.response;

import athiq.veh.isn_backend.model.FileBlob;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingWithItemResponseDTO {
    private long bookingId;
    private String destination;
    private String pickUpLocation;
    private LocalDateTime bookingDate;
    private double price;
    private String phone;
    private String address;
    private String nic;
    private int days;
    private double totalPrice;
    private int status;
    private Long driverId;

    // Vehicle (Item) details
    private String vehicleName;
    private String vehicleDescription;
    private String vehicleMileage;
    private String vehicleFuelType;
    private String vehicleTransmission;
    private String vehicleSeatingCapacity;
    private String vehicleColor;
    private FileBlob vehicleImageUrl;
    private String vehicleYearOfManufacture;
    private String vehicleEngineCapacity;
    private String vehicleFuelEfficiency;
    private String vehicleDeposit;
    private String vehicleLicensePlate;
    // Tax and additional fields
    private Double defaultTaxRate;   // Default Tax Rate (%)
    private Double additionalTaxRate; // Additional Tax Rate (%)
    private Integer defaultTaxDays;

    // Constructor to accept all required parameters
    public BookingWithItemResponseDTO(long bookingId, String destination, String pickUpLocation,
                                      LocalDateTime bookingDate, double price, String phone, String address,
                                      String nic, int days, double totalPrice, int status, Long driverId,
                                      String vehicleName, String vehicleDescription, String vehicleMileage,
                                      String vehicleFuelType, String vehicleTransmission, String vehicleSeatingCapacity,
                                      String vehicleColor, FileBlob vehicleImageUrl, String vehicleYearOfManufacture,
                                      String vehicleEngineCapacity, String vehicleFuelEfficiency, String vehicleDeposit,
                                      String vehicleLicensePlate, Double defaultTaxRate, Double additionalTaxRate, Integer defaultTaxDays) {
        this.bookingId = bookingId;
        this.destination = destination;
        this.pickUpLocation = pickUpLocation;
        this.bookingDate = bookingDate;
        this.price = price;
        this.phone = phone;
        this.address = address;
        this.nic = nic;
        this.days = days;
        this.totalPrice = totalPrice;
        this.status = status;
        this.driverId = driverId;
        this.vehicleName = vehicleName;
        this.vehicleDescription = vehicleDescription;
        this.vehicleMileage = vehicleMileage;
        this.vehicleFuelType = vehicleFuelType;
        this.vehicleTransmission = vehicleTransmission;
        this.vehicleSeatingCapacity = vehicleSeatingCapacity;
        this.vehicleColor = vehicleColor;
        this.vehicleImageUrl = vehicleImageUrl;
        this.vehicleYearOfManufacture = vehicleYearOfManufacture;
        this.vehicleEngineCapacity = vehicleEngineCapacity;
        this.vehicleFuelEfficiency = vehicleFuelEfficiency;
        this.vehicleDeposit = vehicleDeposit;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.defaultTaxRate = defaultTaxRate;
        this.additionalTaxRate = additionalTaxRate;
        this.defaultTaxDays = defaultTaxDays;
    }


    // Getters and Setters (or use Lombok annotations)
}
