package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable //내장 타입. 어딘가에 내장될 수 있다
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() { //JPA스펙상 엔티티나 임베디드 타입(@Embeddable)은 자바 기본 생성자를 public 또는 protected로 설정해야 한다.
        //이유는 JPA구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용할 수 있도록 지원해야 하기 떄문이다.

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
