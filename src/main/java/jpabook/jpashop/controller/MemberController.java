package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller //Controller어노테이션이 있어야 한다.
@RequiredArgsConstructor//final필드를 매개값으로 가지는 생성자를 만든다. 생성자가 하나일 경우 Autowired는 붙이지 안하도 동작한다.
public class MemberController {
    /*뷰와 서버를 왔다갔다 하는 도메인을 만들면 좋지않다. 서버에서 실제 도메인이 원하는 validation이랑 화면에서 넘어올 때 validation이 다를 수 있기 떄문에,
     * 차이가 많기에,, 이럴 때는 새로은 Form객체를 하나 만들어 전달해주는 역할을 하도록 하는 것이 좋다. 어플리케이션이 정말 단순하면 도메인 객체로 받아도 되지만,
     * 웬만하면 Form객체를 쓰는 편이 낫다*/

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/createMemberForm"; //이름이 없는 경우 실행. 나머지 정보들은 memberform에 그대로 담겨 다시 뷰로 간다.
            //다시 입력화면이 나오고 원래 적혀있던 데이터는 다시 뿌려진다
            //thymeleaf + spring 메뉴얼 보고 공부해보자
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members") //뿌릴 때도 엔티티 객체를 뿌리는 것 보다 DTO를 생성해서 뿌리는 걸 권장한다. API를 만들 때는 절대로 엔티티를 외부에 반환하면 안된다.
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("mmm", members);
        return "/members/memberList";
    }
}
