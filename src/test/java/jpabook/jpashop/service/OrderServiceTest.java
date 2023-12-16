package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
class OrderServiceTest { //단위 테스트 할 것. 통합 테스트는 DB연계해서 잘 돌아가는지 최종적인 확인.

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 주문_추가() {
        //given
        Member member = getMember("kim", "서울", "강가", "123-123");
        Book book = getBook("lee", "woowa", 10000, 10);


    /*    Category category1 = new Category(); 카테고리도 추가해 보았다.
        Category category2 = new Category();
        category1.setName("love");
        category2.setName("romance");
        category1.addChildCategory(category2);
        book.addCategory(category1);
*/

        //when

        Long orderId = orderService.order(member.getId(), book.getId(), 2);
        Order findOrder = orderRepository.findOne(orderId);
        assertThat(findOrder.getDelivery().getAddress()).isEqualTo(member.getAddress());
        assertThat(findOrder.getMember()).isEqualTo(member);
        assertThat(book.getStockQuantity()).isEqualTo(8);
        //then
    }

    @Test
    public void 수량초과예외발생() {
        //given
        Member member = getMember("Lee", "seoul", "soha", "123-234");
        Book book = getBook("hese", "ooo", 10000, 10);
        //when

        assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), 11))
                .isInstanceOf(NotEnoughStockException.class);

        assertThatNoException().isThrownBy(() -> orderService.order(member.getId(), book.getId(), 10));

        //then

    }

    @Test
    public void 주문취소() {
        //given
        Member member = getMember("kim", "seoul", "soha", "123-123");
        Book book = getBook("lee", "ooo", 10000, 10);
        Long orderId = orderService.order(member.getId(), book.getId(), 3);
        //when
        orderService.cancel(orderId);
        //then
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    private Book getBook(String author, String bookName, int price, int stockQuanity) {
        Book book = new Book();
        book.setAuthor(author);
        book.setPrice(price);
        book.setName(bookName);
        book.setStockQuantity(stockQuanity);
        em.persist(book);
        return book;
    }

    private Member getMember(String name, String city, String street, String zipcode) {
        Address address = new Address(city, street, zipcode);
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        em.persist(member);
        return member;
    }
}