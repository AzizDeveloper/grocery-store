package dev.aziz.grocerystore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketTotalDto {

    private List<BasketItemDto> basketItemDtos = new ArrayList<>();

    private String wholeBasketPrice;
}
