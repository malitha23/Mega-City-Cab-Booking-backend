package athiq.veh.isn_backend.repository;

import athiq.veh.isn_backend.model.Bookings;
import athiq.veh.isn_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {

    @Query("SELECT b, i FROM Bookings b JOIN b.item i WHERE b.customer = :customer")
    List<Object[]> findByCustomer(@Param("customer") User customer);




}
