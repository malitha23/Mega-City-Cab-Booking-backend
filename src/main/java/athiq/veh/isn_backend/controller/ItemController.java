package athiq.veh.isn_backend.controller;

import athiq.veh.isn_backend.dto.request.CreateItemRequestDTO;
import athiq.veh.isn_backend.dto.response.ItemResponseDTO;
import athiq.veh.isn_backend.mapper.BlobMapper;
import athiq.veh.isn_backend.mapper.UserMapper;
import athiq.veh.isn_backend.model.Item;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.service.AuthService;
import athiq.veh.isn_backend.service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final AuthService authService;

    public ItemController(ItemService itemService, AuthService authService) {
        this.itemService = itemService;
        this.authService = authService;
    }


    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        Optional<Item> itemOptional = itemService.findById(id);

        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();

            ItemResponseDTO responseDTO = new ItemResponseDTO(
                    item.getId(),
                    item.getDescription(),
                    item.getName(),
                    item.getIsBooked(),
                    item.getMileage(),
                    item.getFuelType(),
                    item.getPrice(),
                    item.getTransmission(),
                    item.getSeatingCapacity(),
                    item.getLuggageCapacity(),
                    item.getColor(),
                    item.getYearOfManufacture(),
                    item.getEngineCapacity(),
                    item.getFuelEfficiency(),
                    item.getDeposit(),
                    item.getStatus(),
                    item.getLicensePlate(),

                    item.getTags(),
                    BlobMapper.INSTANCE.toDto(item.getImageBlob()),
                    item.getSubcategory(),
                    item.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(item.getCreatedBy()),
                    UserMapper.INSTANCE.toUserMinDTO(item.getLastModifiedBy()),
                    item.getCreatedDatetime(),
                    item.getLastModifiedDatetime()
            );

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //    @GetMapping("/subcategory/{subcategoryId}")
//    public List<Item> getItemsBySubcategoryId(@PathVariable Integer subcategoryId) {
//        return itemService.findBySubcategoryId(subcategoryId);
//    }
@GetMapping("/subcategory/{subcategoryId}")
public ResponseEntity<List<ItemResponseDTO>> getItemsBySubcategoryId(@PathVariable Integer subcategoryId) {
    List<ItemResponseDTO> items = itemService.findItemsBySubcategoryId(subcategoryId);
    return ResponseEntity.ok(items);
}



    @GetMapping("/category/{categoryId}")
    public List<Item> getItemsByCategoryId(@PathVariable Integer categoryId) {
        return itemService.findByCategoryId(categoryId);
    }

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ItemResponseDTO> addItem(
            @RequestPart(name = "data") CreateItemRequestDTO requestDTO,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(this.itemService.createItem(requestDTO, file));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable Long id, @RequestPart("data") CreateItemRequestDTO requestDTO, @RequestPart("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok(this.itemService.update(id, requestDTO, file));


    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemService.findById(id).isPresent()) {
            itemService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<ItemResponseDTO>> getUserItems() {
        // Get the currently authenticated user
        User currentUser = authService.getCurrentLoggedUser();

        // Fetch items created by the logged-in user
        List<ItemResponseDTO> userItems = itemService.getItemsByUser(currentUser);

        return ResponseEntity.ok(userItems);
    }

    @PutMapping("/{id}/namedesc")
    public ResponseEntity<ItemResponseDTO> updateItemNameAndDescription(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates) {

        // Validate input
        if (!updates.containsKey("name") && !updates.containsKey("description")&& !updates.containsKey("mileage") && !updates.containsKey("fuelType")&& !updates.containsKey("price")
        && updates.containsKey("transmission")&& updates.containsKey("seatingCapacity")&& updates.containsKey("luggageCapacity")&& updates.containsKey("color")&& updates.containsKey("yearOfManufacture")
                && updates.containsKey("engineCapacity")&& updates.containsKey("fuelEfficiency")&& updates.containsKey("deposit") &&updates.containsKey("status") &&updates.containsKey("licensePlate")) {
            return ResponseEntity.badRequest().build();
        }

        // Update the item
        ItemResponseDTO updatedItem = itemService.updateNameAndDescription(
                id,
                updates.get("name"),
                updates.get("description"),
                updates.get("mileage"),
                updates.get("fuelType"),
                updates.get("price"),
                updates.get("transmission"),
                updates.get("seatingCapacity"),
                updates.get("luggageCapacity"),
                updates.get("color"),
                updates.get("yearOfManufacture"),
                updates.get("engineCapacity"),
                updates.get("fuelEfficiency"),
                updates.get("deposit"),
                updates.get("status"),
                updates.get("licensePlate")

        );

        return ResponseEntity.ok(updatedItem);
    }

}

