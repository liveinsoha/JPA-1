package jpabook.jpashop.repository.order.simpleQuery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    /**
     * 일반적인 SQL을 사용할 때 처럼 원하는 값을 선택해서 조회
     * `new` 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환
     * SELECT 절에서 원하는 데이터를 직접 선택하므로 DB 애플리케이션 네트웍 용량 최적화(생각보다 미비)
     * 리포지토리 재사용성 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점
     */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        String jpql = "select" +
                " new jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d";
        return em.createQuery(jpql, OrderSimpleQueryDto.class).getResultList();


    }

    /**
     * OrderRepository는 순수하게 Order 엔티티를 조회하는 데만 사용하고
     * 이 리포지토리는 api에 의존적인 쿼리를 날리므로 따로 분리하여 클래스로 나타내었다.
     */

}
