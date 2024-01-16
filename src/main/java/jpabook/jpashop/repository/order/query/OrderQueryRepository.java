package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orders = findOrders();
        orders.forEach(order -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(order.getOrderId());
            order.setOrderItems(orderItems);
        });
        return orders;
    }

    private List<OrderQueryDto> findOrders() {

        String jpql = "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, d.address, o.status)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d";
        return em.createQuery(jpql, OrderQueryDto.class)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        String jpql = "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :orderId";
        return em.createQuery(jpql, OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();

    }

    public List<OrderQueryDto> findOrderQueryDtos_optimize() {
        List<OrderQueryDto> orders = findOrders();

        List<Long> orderIds = getOrderIds(orders);

        Map<Long, List<OrderItemQueryDto>> orderItems = findOrderItems(orderIds);
        orders.forEach(order -> {
            List<OrderItemQueryDto> orderItemQueryDtos = orderItems.get(order.getOrderId());
            order.setOrderItems(orderItemQueryDtos);
        });

        return orders;
    }

    private List<Long> getOrderIds(List<OrderQueryDto> orders) {
        return orders.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItems(List<Long> orderIds) {
        String jpql = "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id in :orderIds";
        List<OrderItemQueryDto> orderItems = em.createQuery(jpql, OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }


}
