package athiq.veh.isn_backend.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Item extends AbstractAuditable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_blob", referencedColumnName = "id")
    private FileBlob imageBlob;

    private String description;

    private String name;

    private Boolean isBooked;

    private String mileage;

    private String fuelType;

    private String price;

    private String transmission;

    private String seatingCapacity;

    private String luggageCapacity;

    private String color;

    private String yearOfManufacture;

    private String engineCapacity;

    private String fuelEfficiency;

    private String deposit;

    private String status;

    private String licensePlate;

    // ✅ Added tax fields
    @Column(nullable = true, columnDefinition = "DECIMAL(5,2) DEFAULT 0.00")
    private Double defaultTaxRate;  // Default Tax Rate (%)

    @Column(nullable = true, columnDefinition = "DECIMAL(5,2) DEFAULT 0.00")
    private Double additionalTaxRate;  // Additional Tax Rate (%)

    @Column(nullable = true, columnDefinition = "INT DEFAULT 3")
    private Integer defaultTaxDays;  // Default Tax Days

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "item_tag",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tags;


    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subcategory;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


}
