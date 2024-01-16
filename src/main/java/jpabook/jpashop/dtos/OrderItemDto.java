package jpabook.jpashop.dtos;

import jpabook.jpashop.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {

    private Long id;
    ItemDto item;
    private int orderPrice;
    private int count;

}
