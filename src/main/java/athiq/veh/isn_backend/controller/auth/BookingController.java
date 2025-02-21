package athiq.veh.isn_backend.controller.auth;

import athiq.veh.isn_backend.dto.request.RequestBookingDto;
import athiq.veh.isn_backend.dto.response.BookingWithItemResponseDTO;
import athiq.veh.isn_backend.dto.response.ResponseBookingDto;
import athiq.veh.isn_backend.model.Bookings;
import athiq.veh.isn_backend.model.Item;
import athiq.veh.isn_backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<ResponseBookingDto> createBooking(@RequestBody RequestBookingDto requestDto,
                                                            @RequestHeader("Authorization") String token) {
        ResponseEntity<Bookings> bookingResponse = bookingService.createBooking(requestDto, token);

        if (bookingResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (bookingResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        Bookings booking = bookingResponse.getBody();
        assert booking != null;
        ResponseBookingDto responseDto = convertToResponseBookingDto(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/user")
    public ResponseEntity<List<BookingWithItemResponseDTO>> getUserBookings(@RequestHeader("Authorization") String token) {
        List<Bookings> userBookings = bookingService.getUserBookings(token);

        if (userBookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Convert each Booking object into a BookingWithItemResponseDTO
        List<BookingWithItemResponseDTO> responseDtos = userBookings.stream()
                .map(this::convertToBookingWithItemResponseDTO) // Use appropriate conversion method
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    private BookingWithItemResponseDTO convertToBookingWithItemResponseDTO(Bookings booking) {
        // Assuming you also want to include the associated `Item` details
        Item item = booking.getItem(); // or whatever method you use to access the associated item

        return new BookingWithItemResponseDTO(
                booking.getBookingId(),
                booking.getDestination(),
                booking.getPickUpLocation(),
                booking.getBookingDate(),
                booking.getPrice(),
                booking.getPhone(),
                booking.getAddress(),
                booking.getNic(),
                booking.getDays(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getDriverId(),

                // Vehicle (Item) details
                item.getName(),
                item.getDescription(),
                item.getMileage(),
                item.getFuelType(),
                item.getTransmission(),
                item.getSeatingCapacity(),
                item.getColor(),
                item.getYearOfManufacture(),

                // Add additional fields for vehicle
                item.getEngineCapacity(),         // assuming `Item` class has these methods
                item.getFuelEfficiency(),         // assuming `Item` class has these methods
                item.getDeposit(),                // assuming `Item` class has these methods
                item.getLicensePlate(),           // assuming `Item` class has these methods

                // Add tax and additional fields (if required from your context)
                item.getDefaultTaxRate(),      // assuming `Booking` class has these methods
                item.getAdditionalTaxRate(),   // assuming `Booking` class has these methods
                item.getDefaultTaxDays()       // assuming `Booking` class has these methods
        );
    }


    private ResponseBookingDto convertToResponseBookingDto(Bookings booking) {
        ResponseBookingDto responseDto = new ResponseBookingDto();
        responseDto.setBookingId(booking.getBookingId());
        responseDto.setCustomerId(booking.getCustomer().getId());
        responseDto.setDestination(booking.getDestination());
        responseDto.setPickUpLocation(booking.getPickUpLocation());
        responseDto.setBookingDate(booking.getBookingDate());
        responseDto.setPrice(booking.getPrice());
        responseDto.setPhone(booking.getPhone());
        responseDto.setAddress(booking.getAddress());
        responseDto.setNic(booking.getNic());
        responseDto.setDays(booking.getDays());
        responseDto.setTotalPrice(booking.getTotalPrice());
        responseDto.setStatus(booking.getStatus());
        responseDto.setDriverId(booking.getDriverId());

        // Safe check for item presence when assigning vehicleId
        responseDto.setVehicleId(booking.getItem() != null ? booking.getItem().getId() : null);

        return responseDto;
    }
}
