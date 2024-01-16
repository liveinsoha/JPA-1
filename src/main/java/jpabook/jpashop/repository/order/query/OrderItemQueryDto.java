package jpabook.jpashop.repository.order.query;

import lombok.Data;

@Data
public class OrderItemQueryDto {
    Long orderId;
    String itemName;
    int orderPrice;
    int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}