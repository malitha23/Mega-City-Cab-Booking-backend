package athiq.veh.isn_backend.repository;

import athiq.veh.isn_backend.model.Item;
import athiq.veh.isn_backend.model.Saved;
import athiq.veh.isn_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedRepository extends JpaRepository<Saved, Long> {

    List<Saved> findByCreatedBy(User user);


    Optional<Saved> findByItemAndCreatedBy(Item item, User user);


    void deleteAllByItem(Item item);
}
