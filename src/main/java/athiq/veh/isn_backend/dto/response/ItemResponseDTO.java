package athiq.veh.isn_backend.dto.response;

import athiq.veh.isn_backend.dto.auth.UserMinDTO;
import athiq.veh.isn_backend.model.Category;
import athiq.veh.isn_backend.model.SubCategory;
import athiq.veh.isn_backend.model.Tag;

import java.time.LocalDateTime;
import java.util.Set;

public record ItemResponseDTO(
        long id,
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
        String engineCapacity,
        String fuelEfficiency,
        String deposit,
        String status,
        String licensePlate,


        Set<Tag> tags,
        BlobResponseDTO imageBlob,
        SubCategory subCategory,
        Category category,
        UserMinDTO createdBy,
        UserMinDTO lastModifiedBy,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt

) {
}
