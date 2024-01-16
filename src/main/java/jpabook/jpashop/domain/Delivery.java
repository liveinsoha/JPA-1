package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {

    public Delivery() {

    }

    public Delivery(Address address, DeliveryStatus deliveryStatus) {
        this.address = address;
        this.deliveryStatus = deliveryStatus;
    }

    @Id
    @Column(name = "delivery_id")
    @GeneratedValue
    Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    Order order;

    @Embedded
    Address address;

    @Enumerated(EnumType.STRING) //EnumType.ORDINAL을 쓰면 숫자로 매개한다 ->  중간에 뭐가 들어기면 밀리므로 쓰지말자
    DeliveryStatus deliveryStatus; //READY, CAMP
}
