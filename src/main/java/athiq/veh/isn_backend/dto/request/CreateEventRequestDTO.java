package athiq.veh.isn_backend.dto.request;

import athiq.veh.isn_backend.model.Category;
import athiq.veh.isn_backend.model.SubCategory;

public record CreateEventRequestDTO(
        String description,
        String title,
        SubCategory subCategory,
        Category category
) {
}