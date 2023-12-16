package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter //세터주입이다
public class MemberForm {


    @NotEmpty(message = "이름은 필수 입력입니다")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
