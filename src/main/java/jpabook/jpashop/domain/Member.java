package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // '내장 타입을 포함했다'라는 의미
    private Address address;

    @OneToMany(mappedBy = "member")//나는 Order테이블에 있는 member필드 위에서 매핑 된거야. 내가 매핑을 하는게 아니라
    //매핑된 거울일 뿐이야
    private List<Order> orders = new ArrayList<>();



    //Member랑 Order랑 동급으로(클래스 레벨) 놓고 고민을 해야한다. 멤버를 통해 주문이 일어나는 발상은 사람생각이다.
    //Member와 Order의 관계는 일대다 관계인데, 주문을 생성할 떄 회원이 필요하다고 보는 게 시스템의 입장에선 올다
    //주문내역이 필요하면 Order에 있는 member가 필터링 조건이 되아 찾아가면 된다
    //사실상 Member에서 Order로의 일대다 관계가 표현된 List<Order>컬렉션은 필요가 없다.
}
