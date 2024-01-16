package jpabook.jpashop.dtos;

import jpabook.jpashop.api.MemberApiController;
import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private List<OrderItemDto> orderItemDtos = new ArrayList<>();
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

}