package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional //@Transactional이 테스트 케이스에 있으면 테스트 끝난 후 롤백한다
    @Rollback(value = false) // 롤백 하는걸 취소한다. 데이터 저장함.
    public void memberTest() throws Exception {
        //given
        Member member = new Member();
        member.setName("asd");
        //when
        memberRepository.save(member);
        Member one = memberRepository.findOne(member.getId());
        //then

        assertThat(member.getName()).isEqualTo(member.getName());

        //같은 영속성 컨텍스트 안에서는 같은 id를 가지면 같은 객체로 취급한다
        assertThat(member).isEqualTo(member);
    }

}