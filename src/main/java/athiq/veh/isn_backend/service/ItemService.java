package athiq.veh.isn_backend.service;

import org.springframework.transaction.annotation.Transactional;
import athiq.veh.isn_backend.dto.request.CreateItemRequestDTO;
import athiq.veh.isn_backend.dto.response.ItemResponseDTO;
import athiq.veh.isn_backend.mapper.BlobMapper;
import athiq.veh.isn_backend.mapper.UserMapper;
import athiq.veh.isn_backend.model.FileBlob;
import athiq.veh.isn_backend.model.Item;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.ItemRepository;
import athiq.veh.isn_backend.repository.SavedRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Value("${spring.app.localBlobDirectory}")
    private String FILE_DIRECTORY;
    private final ItemRepository itemRepository;
    private final BlobService blobService;
    private final SavedRepository savedRepository;


    public ItemService(ItemRepository itemRepository, BlobService blobService, SavedRepository savedRepository) {
        this.itemRepository = itemRepository;
//        this.itemRepository = itemRepository;
        this.blobService = blobService;
        this.savedRepository = savedRepository;
    }

    public List<ItemResponseDTO> findAll() {
        var itemList = itemRepository.findAll();
        var itemResponseDTOList = new ArrayList<ItemResponseDTO>();
        for (var item : itemList) {
            itemResponseDTOList.add(new ItemResponseDTO(
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
                    item.getDefaultTaxRate(),   // ✅ Added Default Tax Rate (%)
                    item.getAdditionalTaxRate(), // ✅ Added Additional Tax Rate (%)
                    item.getDefaultTaxDays(),   // ✅ Added Default Tax Days
                    item.getTags(),
                    BlobMapper.INSTANCE.toDto(item.getImageBlob()),
                    item.getSubcategory(),
                    item.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(item.getCreatedBy()),
                    UserMapper.INSTANCE.toUserMinDTO(item.getLastModifiedBy()),
                    item.getCreatedDatetime(),
                    item.getLastModifiedDatetime()
            ));
        }
        return itemResponseDTOList;

    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findBySubcategoryId(Integer subcategoryId) {
        return itemRepository.findBySubcategoryId(subcategoryId);
    }

    public List<Item> findByCategoryId(Integer categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }

    public ItemResponseDTO createItem(CreateItemRequestDTO requestDTO, MultipartFile file) throws IOException {
        FileBlob fileBlob = this.blobService.saveBlobToLocal(FILE_DIRECTORY, file);
        var newItem = new Item();
        newItem.setCategory(requestDTO.category());
        newItem.setImageBlob(fileBlob);
        newItem.setName(requestDTO.name());
        newItem.setTags(requestDTO.tags());
        newItem.setDescription(requestDTO.description());
        newItem.setSubcategory(requestDTO.subCategory());
        newItem.setIsBooked(false);

        var savedItem = this.itemRepository.save(newItem);
        return new ItemResponseDTO(
                savedItem.getId(),
                savedItem.getDescription(),
                savedItem.getName(),
                savedItem.getIsBooked(),
                savedItem.getMileage(),
                savedItem.getFuelType(),
                savedItem.getPrice(),
                savedItem.getTransmission(),
                savedItem.getSeatingCapacity(),
                savedItem.getLuggageCapacity(),
                savedItem.getColor(),
                savedItem.getYearOfManufacture(),
                savedItem.getEngineCapacity(),
                savedItem.getFuelEfficiency(),
                savedItem.getDeposit(),
                savedItem.getStatus(),
                savedItem.getLicensePlate(),
                savedItem.getDefaultTaxRate(),   // ✅ Added Default Tax Rate (%)
                savedItem.getAdditionalTaxRate(), // ✅ Added Additional Tax Rate (%)
                savedItem.getDefaultTaxDays(),   // ✅ Added Default Tax Days
                savedItem.getTags(),
                BlobMapper.INSTANCE.toDto(savedItem.getImageBlob()),
                savedItem.getSubcategory(),
                savedItem.getCategory(),
                UserMapper.INSTANCE.toUserMinDTO(savedItem.getCreatedBy()),
                UserMapper.INSTANCE.toUserMinDTO(savedItem.getLastModifiedBy()),
                savedItem.getCreatedDatetime(),
                savedItem.getLastModifiedDatetime()
        );
    }

    public ItemResponseDTO update(Long id, CreateItemRequestDTO requestDTO, MultipartFile file) throws IOException {
        FileBlob fileBlob = this.blobService.saveBlobToLocal(FILE_DIRECTORY, file);
        Optional<Item> optionalItem = this.itemRepository.findById(id);

        if (optionalItem.isPresent()) {
            var itemToBeSaved = optionalItem.get();
            if (requestDTO.name() != null) {
                itemToBeSaved.setName(requestDTO.name());
            }
            if (requestDTO.description() != null) {
                itemToBeSaved.setDescription(requestDTO.description());
            }
            if (requestDTO.isBooked() != null) {
                itemToBeSaved.setIsBooked(requestDTO.isBooked());
            }
            if (requestDTO.tags() != null) {
                itemToBeSaved.setTags(requestDTO.tags());
            }
            if (requestDTO.subCategory() != null) {
                itemToBeSaved.setSubcategory(requestDTO.subCategory());
            }
            if (requestDTO.category() != null) {
                itemToBeSaved.setCategory(requestDTO.category());
            }
            if (requestDTO.mileage() != null) {
                itemToBeSaved.setMileage(requestDTO.mileage());
            }
            if (requestDTO.fuelType() != null) {
                itemToBeSaved.setFuelType(requestDTO.fuelType());
            }
            if (requestDTO.price() != null) {
                itemToBeSaved.setPrice(requestDTO.price());
            }
            itemToBeSaved.setImageBlob(fileBlob);

            var updatedItem = this.itemRepository.save(itemToBeSaved);
            return new ItemResponseDTO(
                    updatedItem.getId(),
                    updatedItem.getDescription(),
                    updatedItem.getName(),
                    updatedItem.getIsBooked(),
                    updatedItem.getMileage(),
                    updatedItem.getFuelType(),
                    updatedItem.getPrice(),
                    updatedItem.getTransmission(),
                    updatedItem.getSeatingCapacity(),
                    updatedItem.getLuggageCapacity(),
                    updatedItem.getColor(),
                    updatedItem.getYearOfManufacture(),
                    updatedItem.getEngineCapacity(),
                    updatedItem.getFuelEfficiency(),
                    updatedItem.getDeposit(),
                    updatedItem.getStatus(),
                    updatedItem.getLicensePlate(),
                    updatedItem.getDefaultTaxRate(),   // ✅ Added Default Tax Rate (%)
                    updatedItem.getAdditionalTaxRate(), // ✅ Added Additional Tax Rate (%)
                    updatedItem.getDefaultTaxDays(),   // ✅ Added Default Tax Days
                    updatedItem.getTags(),
                    BlobMapper.INSTANCE.toDto(updatedItem.getImageBlob()),
                    updatedItem.getSubcategory(),
                    updatedItem.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(updatedItem.getCreatedBy()),
                    UserMapper.INSTANCE.toUserMinDTO(updatedItem.getLastModifiedBy()),
                    updatedItem.getCreatedDatetime(),
                    updatedItem.getLastModifiedDatetime()
            );
        }

        throw new RuntimeException("Item not found");
    }



    @Transactional
    public void deleteById(Long id) {
        // Fetch the item
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Remove related `Saved` entities
        savedRepository.deleteAllByItem(item);

        // Delete the item itself
        itemRepository.delete(item);
    }




    public List<ItemResponseDTO> getItemsByUser(User user) {
        List<Item> items = itemRepository.findByCreatedById(user.getId());
        List<ItemResponseDTO> itemResponseDTOs = new ArrayList<>();
        for (Item item : items) {
            itemResponseDTOs.add(new ItemResponseDTO(
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
                    item.getDefaultTaxRate(),   // ✅ Added Default Tax Rate (%)
                    item.getAdditionalTaxRate(), // ✅ Added Additional Tax Rate (%)
                    item.getDefaultTaxDays(),   // ✅ Added Default Tax Days

                    item.getTags(),
                    BlobMapper.INSTANCE.toDto(item.getImageBlob()),
                    item.getSubcategory(),
                    item.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(item.getCreatedBy()),
                    UserMapper.INSTANCE.toUserMinDTO(item.getLastModifiedBy()),
                    item.getCreatedDatetime(),
                    item.getLastModifiedDatetime()
            ));
        }
        return itemResponseDTOs;
    }

    public List<ItemResponseDTO> findItemsBySubcategoryId(Integer subcategoryId) {
        List<Item> items = itemRepository.findBySubcategoryId(subcategoryId);
        List<ItemResponseDTO> itemResponseDTOs = new ArrayList<>();

        for (Item item : items) {
            itemResponseDTOs.add(new ItemResponseDTO(
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
                    item.getDefaultTaxRate(),   // ✅ Added Default Tax Rate (%)
                    item.getAdditionalTaxRate(), // ✅ Added Additional Tax Rate (%)
                    item.getDefaultTaxDays(),   // ✅ Added Default Tax Days
                    item.getTags(),
                    BlobMapper.INSTANCE.toDto(item.getImageBlob()),
                    item.getSubcategory(),
                    item.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(item.getCreatedBy()),  // Mapping createdBy
                    UserMapper.INSTANCE.toUserMinDTO(item.getLastModifiedBy()),  // Mapping lastModifiedBy
                    item.getCreatedDatetime(),
                    item.getLastModifiedDatetime()
            ));
        }
        return itemResponseDTOs;
    }

    public ItemResponseDTO updateNameAndDescription(Long id, String name, String description, String mileage, String fuelType, String price,
                                                    String transmission, String seatingCapacity, String luggageCapacity, String color, String yearOfManufacture,
                                                    String engineCapacity, String fuelEfficiency, String deposit, String status, String licensePlate
    ) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (name != null) {
            item.setName(name);
        }
        if (description != null) {
            item.setDescription(description);
        }
        if (mileage != null) {
            item.setMileage(mileage);
        }
        if(fuelType != null) {
            item.setFuelType(fuelType);
        }
        if(price != null) {
            item.setPrice(price);
        }
        if(transmission != null) {
            item.setTransmission(transmission);
        }
        if(seatingCapacity != null){
        item.setSeatingCapacity(seatingCapacity);
        }
        if(luggageCapacity != null){
item.setLuggageCapacity(luggageCapacity);
        }
        if(color != null){
item.setColor(color);
        }
        if(yearOfManufacture != null){
item.setYearOfManufacture(yearOfManufacture);
        }
        if(engineCapacity != null){
item.setEngineCapacity(engineCapacity);
        }
        if(fuelEfficiency != null){
item.setFuelEfficiency(fuelEfficiency);
        }
        if(deposit != null){
item.setDeposit(deposit);
        }
        if(status!=null){
item.setStatus(status);

        }
        if(licensePlate!=null){
            item.setLicensePlate(licensePlate);
        }



        // Save the updated item
        Item updatedItem = itemRepository.save(item);

        // Return the response DTO
        return new ItemResponseDTO(
                updatedItem.getId(),
                updatedItem.getDescription(),
                updatedItem.getName(),
                updatedItem.getIsBooked(),
                updatedItem.getMileage(),
                updatedItem.getFuelType(),
                updatedItem.getPrice(),
                updatedItem.getTransmission(),
                updatedItem.getSeatingCapacity(),
                updatedItem.getLuggageCapacity(),
                updatedItem.getColor(),
                updatedItem.getYearOfManufacture(),
                updatedItem.getEngineCapacity(),

                updatedItem.getFuelEfficiency(),
                updatedItem.getDeposit(),
                updatedItem.getStatus(),
                updatedItem.getLicensePlate(),
                item.getDefaultTaxRate(),   // ✅ Added Default Tax Rate (%)
                item.getAdditionalTaxRate(), // ✅ Added Additional Tax Rate (%)
                item.getDefaultTaxDays(),   // ✅ Added Default Tax Days

                updatedItem.getTags(),
                BlobMapper.INSTANCE.toDto(updatedItem.getImageBlob()),
                updatedItem.getSubcategory(),
                updatedItem.getCategory(),
                UserMapper.INSTANCE.toUserMinDTO(updatedItem.getCreatedBy()),
                UserMapper.INSTANCE.toUserMinDTO(updatedItem.getLastModifiedBy()),
                updatedItem.getCreatedDatetime(),
                updatedItem.getLastModifiedDatetime()
        );
    }


}

