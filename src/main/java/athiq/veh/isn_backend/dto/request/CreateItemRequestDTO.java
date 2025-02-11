
package athiq.veh.isn_backend.dto.request;

import athiq.veh.isn_backend.model.Category;
import athiq.veh.isn_backend.model.SubCategory;
import athiq.veh.isn_backend.model.Tag;

import java.util.Set;

public record CreateItemRequestDTO(
        Long id,
        String description,
        String name,
        Boolean isBooked,
        String mileage,
        String fuelType,
        String price,
        String transmission,
        String seatingCapacity,
        String luggageCapacity,
        String color,
        String yearOfManufacture,
        String fuelEfficiency,
        String deposit,
        String status,
        String licensePlate,

        Set<Tag> tags,
        SubCategory subCategory,
        Category category
) {
}