package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 한 테이블에 다 떄려박는 전략.
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items") //나(이 클래스 item)은 상대(category) 매핑되었을 뿐이야
    List<Category> categories = new ArrayList<>();

    public void addStockQuantity(int quantity) {
        stockQuantity += quantity;
    }

    public void removeStockQuantity(int quantity) {
        if (quantity > stockQuantity) {
            throw new NotEnoughStockException("[ERROR] 재고가 부족합니다");
        }
        stockQuantity -= quantity;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

}
