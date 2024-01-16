package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(of = "orderId")
public class OrderQueryDto {
    Long orderId;
    String name;
    Address address;
    OrderStatus orderStatus;
    List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long id, String name, Address address, OrderStatus orderStatus) {
        this.orderId = id;
        this.name = name;
        this.address = address;
        this.orderStatus = orderStatus;
    }
}