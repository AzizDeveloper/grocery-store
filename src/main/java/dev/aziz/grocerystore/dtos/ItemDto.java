package dev.aziz.grocerystore.dtos;

import dev.aziz.grocerystore.entities.Category;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {

    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    private String description;

    @NotEmpty(message = "Price should not be empty")
    private String price;

    private Category category;

    @NotEmpty(message = "Picture should not be empty")
    private String pictureUrl;

    @NotEmpty(message = "Weight should not be empty")
    private Integer weight;

    @NotEmpty(message = "Stock amount should not be empty")
    private Integer stockAmount;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;
}
