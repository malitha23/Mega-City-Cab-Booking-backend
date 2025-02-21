package athiq.veh.isn_backend.service;

import athiq.veh.isn_backend.dto.request.RequestBookingDto;
import athiq.veh.isn_backend.dto.response.ErrorResponse;
import athiq.veh.isn_backend.model.Bookings;
import athiq.veh.isn_backend.model.Item;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.BookingRepository;
import athiq.veh.isn_backend.repository.ItemRepository;
import athiq.veh.isn_backend.repository.UserRepository;
import athiq.veh.isn_backend.security.jwt.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;


    public List<Bookings> getUserBookings(String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Get email from JWT token
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

        // Fetch user
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElse(null);

        if (user == null) {
            // Handle case where user is not found
            throw new RuntimeException("User not found for email: " + email);
            // Or return an empty list if you prefer:
            // return new ArrayList<>();
        }

        // Fetch bookings along with their associated items
        List<Object[]> results = bookingRepository.findByCustomer(user);

        List<Bookings> bookings = new ArrayList<>();

        // Process the results and map to Booking objects
        for (Object[] result : results) {
            Bookings booking = (Bookings) result[0];  // First element is the Booking
            Item item = (Item) result[1];             // Second element is the Item

            // You can add additional processing here if needed

            bookings.add(booking); // Add the Booking object to the list
        }

        // Return the list of bookings
        return bookings;
    }



    public ResponseEntity<Bookings> createBooking(RequestBookingDto requestDto, String token) {
        try {
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Get email from JWT token
            String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

            // Fetch user
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            User user = optionalUser.get();

            // Find the item (vehicle)
            Item item = itemRepository.findById(requestDto.getVehicleId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found with ID " + requestDto.getVehicleId()));

            // Create the booking and set its fields
            Bookings booking = new Bookings();
            booking.setCustomer(user);
            booking.setDestination(requestDto.getDestination());
            booking.setPickUpLocation(requestDto.getPickUpLocation());
            booking.setBookingDate(requestDto.getBookingDate());
            booking.setPrice(requestDto.getPrice());
            booking.setPhone(requestDto.getPhone()); // Correct field
            booking.setAddress(requestDto.getAddress()); // Correct field
            booking.setNic(requestDto.getNic()); // Correct field
            booking.setDays(requestDto.getDays()); // Correct field
            booking.setTotalPrice(requestDto.getTotalPrice()); // Correct field
            booking.setItem(item);

            // Set booking status as "pending"
            booking.setStatus(0); // assuming 'false' means pending

            // Save the booking
            Bookings savedBooking = bookingRepository.save(booking);

            // Send email to the user notifying them of the pending approval
            sendBookingPendingEmailToUser(user.getEmail(), user.getFirstName(), savedBooking);

            // Send email to admin/moderator notifying them of the pending booking


          sendBookingPendingEmailToAdmin(savedBooking);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
        } catch (Exception e) {
            System.out.println(e.toString());
            // Return the error response as a ResponseEntity
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public void sendBookingPendingEmailToUser(String userEmail, String fname, Bookings booking) {


        // Get booking ID using getBookingId method
        String bookingId = String.valueOf(booking.getBookingId()); // Assuming getBookingId() returns a String

        // Construct the HTML content for the email body
        String subject = "Booking Pending Approval";
        String body = "<html>" +
                "<body>" +
                "<h2>Dear "+fname+",</h2>" +
                "<p>Your booking with ID <strong>" + bookingId + "</strong> is pending approval by the admin.</p>" +
                "<p><strong>Destination:</strong> " + booking.getDestination() + "</p>" +
                "<p><strong>Pick-Up Location:</strong> " + booking.getPickUpLocation() + "</p>" +
                "<p><strong>Booking Date:</strong> " + booking.getBookingDate() + "</p>" +
                "<p>You will be notified once it's approved. Thank you for your patience.</p>" +
                "<br>" +
                "<p>Best regards,</p>" +
                "<p>Your Team</p>" +
                "</body>" +
                "</html>";

        // Use an email service to send the email (make sure it supports HTML content)
        emailService.sendEmail(userEmail, subject, body);
    }

    public void sendBookingPendingEmailToAdmin(Bookings booking) {
        // Get booking ID using getBookingId method
        String bookingId = String.valueOf(booking.getBookingId()); // Assuming getBookingId() returns a String

        // Find all users with role ROLE_ADMIN or ROLE_MODERATOR (fetch User objects, not just IDs)
        List<User> adminsOrModerators = userRepository.findByRoleIdIn(Arrays.asList(2, 3));

        // Construct the HTML content for the email body
        String subject = "New Booking Pending Approval";
        String body = "<html>" +
                "<body>" +
                "<h2>A new booking is pending approval</h2>" +
                "<p>A new booking with ID <strong>" + bookingId + "</strong> is pending approval. Please review it in the admin panel.</p>" +
                "<p><strong>Destination:</strong> " + booking.getDestination() + "</p>" +
                "<p><strong>Pick-Up Location:</strong> " + booking.getPickUpLocation() + "</p>" +
                "<p><strong>Booking Date:</strong> " + booking.getBookingDate() + "</p>" +
                "<br>" +
                "<p><a href='#' style='background-color: #007bff; color: white; padding: 10px 15px; text-decoration: none; border-radius: 5px;'>Review Booking</a></p>" +
                "<p>Thank you for your prompt attention.</p>" +
                "<br>" +
                "<p>Best regards,</p>" +
                "<p>Your Team</p>" +
                "</body>" +
                "</html>";

        // Send email to all admins and moderators
        for (User adminOrModerator : adminsOrModerators) {
            String adminEmail = adminOrModerator.getEmail();
            emailService.sendEmail(adminEmail, subject, body);
        }
    }




}

