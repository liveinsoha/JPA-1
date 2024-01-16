package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderSimpleController {

    private final OrderService orderService;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        log.info("orderV1");
        List<Order> all = orderService.findOrders(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName(); //LAZY LOADING 강제 초기화
            order.getDelivery().getAddress();
        }
        return all; //배열로 그대로 나가는 건 좋지 않다.한번 감싸서 하기를 추천
    }

    /**
     * 엔티티를 DTO로 변환하는 일반적인 방법이다.
     * 쿼리가 총 1 + N + N번 실행된다. (v1과 쿼리수 결과는 같다.)
     * `order` 조회 1번(order 조회 결과 수가 N이 된다.)
     * `order -> member` 지연 로딩 조회 N 번
     * `order -> delivery` 지연 로딩 조회 N 번
     * 예) order의 결과가 4개면 최악의 경우 1 + 4 + 4번 실행된다.(최악의 경우)
     * 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략한다.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        log.info("orderV2");
        List<Order> orders = orderService.findOrders(new OrderSearch());
        List<SimpleOrderDto> orderDtos = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        return orderDtos;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        log.info("orderV3");
        List<Order> orders = orderService.findOrdersWithMemberDelivery(new OrderSearch());
        /**
         * findOrdersWithMemberDelivery는 원하는 데이터를 fetch조인으로 가져와서 Dto를 생성하여 쓰므로
         * 재사용성이 가능하다.
         */
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        /**
         * Dto에서 엔티티에 의존하는 것은 크게 문제가 되진 않는다.
         */
        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName(); //Lazy 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress(); //Lazy 초기화
        }
    }

    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        log.info("orderV3");
        return orderSimpleQueryRepository.findOrderDtos();
        //얘는 재사용성이 떨어진다
    }

}
