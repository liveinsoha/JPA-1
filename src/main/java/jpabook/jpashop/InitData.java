package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    private void initData() {
        initService.initDataV1();
        initService.initDataV2();
    }

    @RequiredArgsConstructor
    @Component
    @Transactional
    static class InitService {

        private final EntityManager em;
        private final OrderService orderService;

        public void initDataV1() {
            Member member = createMember("Danny", "서울", "www", "eee");
            em.persist(member);

            Book book1 = createBook("시골 JPA", 10000, 100, "Kim", "123123");
            em.persist(book1);

            Book book2 = createBook("도시 JPA", 20000, 200, "Lee", "321312");
            em.persist(book2);

            OrderItem orderItem1 = createOrderItem(book1, 10);
            OrderItem orderItem2 = createOrderItem(book2, 20);
            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void initDataV2() {
            Member member = createMember("Tim", "진주", "www", "eee");
            em.persist(member);

            Book book1 = createBook("시골 Spring", 10000, 100, "Kim", "123123");
            em.persist(book1);

            Book book2 = createBook("도시 Spring", 20000, 200, "Lee", "321312");
            em.persist(book2);

            OrderItem orderItem1 = createOrderItem(book1, 30);
            OrderItem orderItem2 = createOrderItem(book2, 40);
            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            return new Member(name, new Address(city, street, zipcode));
        }


        private Book createBook(String name, int price, int stockQuantity, String author, String isbn) {
            return new Book(name, price, stockQuantity, author, isbn);
        }

        private OrderItem createOrderItem(Item item, int count) {
            return OrderItem.createOrderItem(item, item.getPrice(), count);
        }

        private Delivery createDelivery(Member member) {
            return new Delivery(member.getAddress(), DeliveryStatus.READY);
        }

    }
}
