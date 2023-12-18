package dev.aziz.grocerystore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSummaryDto {

    private Long id;

    private String name;

    private String price;

    private String category;
}
