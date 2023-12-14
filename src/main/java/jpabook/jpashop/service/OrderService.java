package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {


    @Autowired// 생성자가 하나일 떈 @Autowired생략 가능하다.
    private final OrderRepository orderRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final ItemRepository itemRepository;

    @Transactional
    /*@Transactional: 이 어노테이션은 스프링에서 제공하는 어노테이션으로, 메서드에 적용되면 해당 메서드가 하나의 트랜잭션으로 묶이게 됩니다.
    트랜잭션 도중에 예외가 발생하면 롤백이 수행됩니다.*/
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(DeliveryStatus.READY); // 실제로는 배송지 정보 따로 입력
        delivery.setAddress(member.getAddress());

        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public void cancel(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }



}
