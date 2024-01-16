package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //클래스 레벨에서 Transactional을 쓰면 메서드에 기본으로 다 걸려들어간다. 읽기 전용 Transactional사용 최적화

public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional //이 쓰기 기능에는 readOnly=true가 있으면 안된다. 이 애노테이션이 우선순위를 가지므로 쓰기가 가능하다.
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId(); //id값을 돌려주면서 저장을 확인해본다.
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("[ERROR] 이미 존재하는 회원입니다");
        }
    }

    @Transactional
    public void update(Long id, String name){
        Member findMember = memberRepository.findOne(id);
        findMember.setName(name);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) { //비즈니스상 많이 필요한 메서드이다
        return memberRepository.findOne(memberId);
    }
}
