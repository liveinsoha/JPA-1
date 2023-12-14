package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") //order를 예약어로 가지고 있기 떄문에 테이블 명을 지정해준다
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자 protected설정
public class Order {



    @Id
    @GeneratedValue
    @Column(name = "order_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)//orderItem의 필드 order와 매핑. 나(이 클래스)는 orderItem의 필드 order에 매핑된거야
    //내가 매핑한 게 아니라 매핑된 거울일 뿐이야(일대다의 관계) 하나의 order에 여러개의 orderItem이 있다
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //엔티티의 상태변화 전파!
    @JoinColumn(name = "delivery_id")//일대일 관계일 떄, 자주 조회하는 클래스를 연관관계의 주인으로 두는 걸 선호
    private Delivery delivery;

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING) //EnumType.STRING사용할 것.
    private OrderStatus status; //주문 상태 ORDER, CANCEL

    //양방향 연관관계 메서드 원자적으로 수행
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);
        return order;
    }

    public void cancel() {
        if (getDelivery().getDeliveryStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("[ERROR] 배송완료된 상품은 취소할 수 없습니다");
        }
        setStatus(OrderStatus.CANCEL);
        orderItems.forEach(OrderItem::cancel);
    }



    public int getTotalOrderPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
