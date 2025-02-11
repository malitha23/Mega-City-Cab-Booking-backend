package athiq.veh.isn_backend.dto.response;

import athiq.veh.isn_backend.model.Item;
import athiq.veh.isn_backend.model.User;

public class SavedResponseDTO {
    private Item item;
    private User createdBy;

    public SavedResponseDTO( User createdBy) {

        this.createdBy = createdBy;
    }

    public SavedResponseDTO(Item item, User createdBy) {
        this.item = item;
        this.createdBy = createdBy;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
