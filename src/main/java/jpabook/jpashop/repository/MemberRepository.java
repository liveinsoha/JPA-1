package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor //@PersistenceContext도 가능하지만 스프링 부트를 사용하면 @Autowired를 사용하여 주입할 수도 있다
public class MemberRepository {

    @Autowired
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member); //영속성 컨텍스트가 member의 @Id필드 생성 보장
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    } //JPQL.

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        /*setParameter("name", name):JPQL 쿼리에 사용되는 파라미터를 설정합니다. 쿼리에서 :name이라는 파라미터를 설정하고,
        이를 실제 값으로 바인딩합니다. 이때 name은 메서드의 매개변수로 전달된 값이 사용됩니다.*/
    }

}
