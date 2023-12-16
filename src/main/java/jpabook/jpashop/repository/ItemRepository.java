package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {

    @Autowired
    EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
           /* 준영속 엔티티의 값들을 영속 엔티티의 값으로 밀어넣어준다.
            준영속 엔티티가 영속 엔티티가 되는 것은 아니다(값만 받아서 영속 엔티티의 값에 주입하고 버린다).
            .여기서 리턴되는 객체가 영속 엔티티이다.
            merge를 쓰는 것은 위험하다. 필드를 선택적으로 바꿀 수 있는게 아니라 준영속 엔티티의 모든 필드가 반영된다.
            예시로 price가 변경 불가능하여 null이 준영속 엔티티로 들어오면 DB에 있는 영속 엔티티의 price가 null로 업데이트되어 굉장히 위험하다.
            따라서 merge를 쓰지말고 변경감지를 쓰자(Transactional)
            불편하더라도 변경감지로 하나하나 바꾸는 게 낫다.*/

        }
    }

    public Item findOne(Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

    public void update(Item param) { //변경감지를 이용
        if (param.getClass() == Book.class) {
            Book bookParam = (Book) param; //어설프게
            Book findBook = em.find(Book.class, param.getId());
            findBook.setName(bookParam.getName());
            findBook.setPrice(bookParam.getPrice());
            findBook.setStockQuantity(bookParam.getStockQuantity());
            findBook.setAuthor(bookParam.getAuthor());
            findBook.setIsbn(bookParam.getIsbn());
            /*이 변경감지 코드도 절대 좋은 것이 아니다. 우선 setter가 남발되고 있다
            * 의미 있는 메서드 ex : changeStockQuantity처럼 객체 내부에서 업데이트 로직을 가지고 있는 것이 객체지향적이다!
            * 엔티티 레벨에서 보고 추적할 수 있도록 설계를 해야 한다*/
        }
    }

}
