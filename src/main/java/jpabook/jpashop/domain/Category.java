package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    Long id;

    String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")) //다대다 관계를 일대다 다대일로 풀어내는 중간 테이블이 있어야 한다. -> 실무에서 안 씀
    List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") //부모 카테고리, 하나. 콜룸 생성
    Category parent;

    @OneToMany(mappedBy = "parent") //자식 카테고리 여러개. 재밌다 연관관계. 다른 엔티티랑 매핑하듯이 하면 된다.
            //나(이 클래스는)매핑되었을 뿐이야 상대(categoryu)의 parent에 매핑되어 있음.
    List<Category> child = new ArrayList<>();


    public void addChildCategory(Category child) {//연관관계 메서드
        this.child.add(child);
        child.setParent(this);
    }
}
