package jpabook.jpashop.repository.order.simpleQuery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime
            orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    /**
     * jpql에서 엔티티가 넘어오는 문법은 식별자가 넘어오기 때문에 파라미터로 Order order를 받을 수 없다!!
     * 파라미터로 엔티티 대신에 값들을 받자
     */
}
