package jpabook.jpashop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
}
