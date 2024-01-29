package dev.aziz.grocerystore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketItemDto {

    private String name;

    private Integer stockAmount;

    private String unitPrice;

    private String totalPrice;

}
