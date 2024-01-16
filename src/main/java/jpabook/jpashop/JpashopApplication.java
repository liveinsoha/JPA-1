package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

    public static void main(String[] args) {

        SpringApplication.run(JpashopApplication.class, args);
    }

    /**
     * order` `member` 와 `order` `delivery` 는 지연 로딩이다. 따라서 실제 엔티티 대신에 프록시 존재
     * jackson 라이브러리는 기본적으로 이 프록시 객체를 json으로 어떻게 생성해야 하는지 모름 예외 발생
     * `Hibernate5Module` 을 스프링 빈으로 등록하면 해결(스프링 부트 사용중)
     */
    @Bean
    Hibernate5JakartaModule hibernate5Module() {
        Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
        //hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5JakartaModule;
    }
    /**
     * 기본 설정이 LAZY LOADING일 경우 null을 반환하지만 configure 설정을 통하여 가져올 수 있다
     * Lazy Loading이 걸려있는 애들 다 찔러서 DB에서 가져온다.. 쿼리가 무지하게 나가기 떄문에 성능상 문제가 있다.
     */


}
