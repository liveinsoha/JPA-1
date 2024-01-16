package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dtos.MemberDto;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 뷰 전용 컨트롤러랑 api전용 컨트롤러는 패키지를 분리하는게 좋다. 에러가 났을 때 반환해야할 공통 스펙이 다르기 때문.
 */
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 문제점
     * 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등)
     * 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 모든 요청
     * 요구사항을 담기는 어렵다.
     * 엔티티가 변경되면 API 스펙이 변한다.
     * 결론
     * API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * CreateMemberRequest` 를 `Member` 엔티티 대신에 RequestBody와 매핑한다.
     * 엔티티와 프레젠테이션 계층을 위한 로직을 분리할 수 있다.
     * 엔티티와 API 스펙을 명확하게 분리할 수 있다.
     * 엔티티가 변해도 API 스펙이 변하지 않는다.
     * 참고: 실무에서는 엔티티를 API 스펙에 노출하면 안된다!
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    static class CreateMemberRequest {
        private String name;

        public CreateMemberRequest() {
        }

        /**
         * 엔티티만 보고 API로 부터 어떤 스펙들을 받을지 판단을 할 수 없다.
         * 하지만 DTO를 사용하면 DTO를 보고 클라이언트가 보내오는 API의 스펙을 파악할 수 있고,
         * 유효성 검사도 DTO클래스에 @NotEmpty를 사용하여 검증을 할 수 있다
         * 멤버에서 유효성 검사를 하면 APi스펙에 의존적인 엔티티가 되어버린다..
         *
         * @param name
         */
        public CreateMemberRequest(String name) {
            this.name = name;
        }
    }

    /**
     * 수정할 떄는 변경감지를 이용하는 게 좋다.
     */
    @PutMapping("/api/v2/update/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName()); //여기서 Transaction을 걸고 update를 마친다
        Member findMember = memberService.findOne(id);
        /**
         * 업데이트 역할은 업데이트 역할에 충실하고 찾아오는 로직은 따로 작성하여 findOne으로 가져온다.
         */
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /**
     * 생성, 수정 용 별도의 DTO클래스를 생성한다. 스펙이 다르기 때문이다.
     * DTO는 데이터가 왔다갔다하는 클래스이고 비즈니스 로직이 있는 것도 아니기 떄문에
     * 롬복 애노테이션을 막 쓰는 것도 괜찮게 생각한다. (Getter Setter등등)
     */
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한
     * 문제점
     * 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * 기본적으로 엔티티의 모든 값이 노출된다.
     * 응답 스펙을 맞추기 위해 로직이 추가된다. (@JsonIgnore, 별도의 뷰 로직 등등)
     * 실무에서는 같은 엔티티에 대해 API가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위
     * 한 프레젠테이션 응답 로직을 담기는 어렵다.
     * 엔티티가 변경되면 API 스펙이 변한다.
     * 추가로 컬렉션을 직접 반환하면 항후 API 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로 해결)
     * 결론
     * API 응답 스펙에 맞추어 별도의 DTO를 반환한다.
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result<List<MemberDto>> membersV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(Member::toDto)
                .toList();
        return new Result<>(collect.size(), collect);
    }


    /**
     * 리스트로 API가 바로 나가면 JSON이 배열타입으로 나가고 유연성이 떨어지기 떄문에 REsult클래스로 감싸서 반환
     * 나중에 요구사항이 추가 도면 쉽게 수정할 수 있다
     *
     * @param <T>
     */
    @Data
    @AllArgsConstructor //껍데기 클래스
    static class Result<T> {
        private int count;
        private T data;
    }

}
