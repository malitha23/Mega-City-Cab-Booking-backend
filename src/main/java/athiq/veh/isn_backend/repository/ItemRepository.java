package athiq.veh.isn_backend.repository;

import athiq.veh.isn_backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findBySubcategoryId(Integer subcategoryId);
//    List<Item> findItemByCreatedById(Long id);
    List<Item> findByCategoryId(Integer categoryId);
    List<Item> findByCreatedById(Long userId);
}
