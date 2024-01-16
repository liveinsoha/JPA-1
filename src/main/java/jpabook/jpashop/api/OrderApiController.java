package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.dtos.OrderItemDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName()); //Lazy 초기화
        }
        return orders;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        List<OrderDto> orderDtos = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return orderDtos;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderService.findOrdersWithItem();
        for (Order order : orders) {
            System.out.println("order = " + order + "id = " + order.getId());
        }
        List<OrderDto> orderDtos = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return orderDtos;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "1000") int limit) {
        List<Order> orders = orderService.findOrderWithItem(offset, limit);
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    private final OrderQueryRepository orderQueryRepository;


    /**
     * Query: 루트 1번, 컬렉션 N 번 실행
     * ToOne(N:1, 1:1) 관계들을 먼저 조회하고, ToMany(1:N) 관계는 각각 별도로 처리한다.
     * 이런 방식을 선택한 이유는 다음과 같다.
     * ToOne 관계는 조인해도 데이터 row 수가 증가하지 않는다.
     * ToMany(1:N) 관계는 조인하면 row 수가 증가한다.
     * row 수가 증가하지 않는 ToOne 관계는 조인으로 최적화 하기 쉬우므로 한번에 조회하고, ToMany 관계는 최적
     * 화 하기 어려우므로 `findOrderItems()` 같은 별도의 메서드로 조회한다
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }


    /**
     * Query: 루트 1번, 컬렉션 1번
     * ToOne 관계들을 먼저 조회하고, 여기서 얻은 식별자 orderId로 ToMany 관계인 `OrderItem` 을 한꺼번에 조
     * 회
     * MAP을 사용해서 매칭 성능 향상(O(1))
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findOrderQueryDtos_optimize();
    }

    @Data
    static class OrderDto {
        private Long id;
        private String name;
        private Address address;
        private LocalDateTime orderDate;
        private List<OrderItemDto> orderItems;
        private OrderStatus status;

        public OrderDto(Order order) {
            this.id = order.getId();
            this.name = order.getMember().getName();
            this.address = order.getDelivery().getAddress();
            this.orderDate = order.getOrderDate();
            this.status = order.getStatus();
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int count;
        private int orderPrice;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.count = orderItem.getCount();
            this.orderPrice = orderItem.getOrderPrice();
        }
    }

}
