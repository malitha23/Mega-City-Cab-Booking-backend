package athiq.veh.isn_backend.repository;

import athiq.veh.isn_backend.model.Item;
import athiq.veh.isn_backend.model.User;
import org.eclipse.angus.mail.imap.protocol.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(Long id);

    List<Item> findBySubcategoryId(Integer subcategoryId);
//    List<Item> findItemByCreatedById(Long id);
    List<Item> findByCategoryId(Integer categoryId);
    List<Item> findByCreatedById(Long userId);

    @Query("SELECT i FROM Item i WHERE " +
            "(:categoryId IS NULL OR i.category.id = :categoryId) AND " +
            "(:subcategoryId IS NULL OR i.subcategory.id = :subcategoryId) AND " +
            "(LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL)")
    List<Item> findItemsBySearchCriteria(
            @Param("categoryId") Long categoryId,
            @Param("subcategoryId") Long subcategoryId,
            @Param("name") String name);




}
