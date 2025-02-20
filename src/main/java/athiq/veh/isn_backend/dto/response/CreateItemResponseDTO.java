package athiq.veh.isn_backend.dto.response;

import athiq.veh.isn_backend.dto.auth.UserMinDTO;
import athiq.veh.isn_backend.model.FileBlob;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateItemResponseDTO(
        Long id,
        String name,
        String description,
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

        Double defaultTaxRate,   // ✅ Added Default Tax Rate (%)
        Double additionalTaxRate, // ✅ Added Additional Tax Rate (%)
        Integer defaultTaxDays,   // ✅ Added Default Tax Days

        Integer categoryId,
        Integer subCategoryId,
        Set<FileBlob> fileBlobs,
        UserMinDTO createdBy,
        UserMinDTO lasModifiedBy,
        LocalDateTime createdDateTime,
        LocalDateTime lastModifiedDateTIme
) {
}
