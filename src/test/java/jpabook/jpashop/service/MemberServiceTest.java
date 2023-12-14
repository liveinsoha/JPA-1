package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional //JPA에서 같은 Transaction안에서 @Id(PK)값이 영속성 컨택스트가 똑같은 객체가 관리된다.
class MemberServiceTest { //테스트를 진행해도 insert쿼리는 나가지 않는다. Transaction에서 Commit될 때 최종적으로 insert문이 나가면서 데이터베이스에 반영된다.
    //여기서는 롤백을 기본적으로 하기떄문에 insert문이 안나간다.
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;


    //em.flush를 이용해서 영속성 컨텍스트에 있는 변경이나 등록내용을 데이터베이스에 반영함을 의미한다.
    //em.flush를 하면 isert문을 날려 데이터베이스에 하여 반영하고. @Transactional이 다시 롤백한다.(insert문 후에 다시 복원)
    @Test
    void 회원가입() {

        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member);
        em.flush();


        Member one = memberService.findOne(savedId);
        assertThat(one).isEqualTo(member);
    }

    @Test
    public void 중복확인() {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        //then
        assertThatThrownBy(() -> memberService.join(member2)).isInstanceOf(IllegalStateException.class);
    }
}