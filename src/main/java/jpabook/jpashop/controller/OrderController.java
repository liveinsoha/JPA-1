package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController { //폼-서밋 방식

    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping("/order")
    public String createOrder(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findAll();
        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam Long memberId,
                        @RequestParam Long itemId,
                        @RequestParam int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders"; //주문 완료 후 주문 내역으로 이동
    }
    /*컨트롤러 계층에서는 id만 받아서 서비스 게층으로 넘기고 Transactional이 걸려있는 서비스 계층에서 로직을 실행하는 게 낫다
     * 그래야 영속 엔티티를 다루며 변경값이 있을 때 @Transactional아래에서 영속성 컨텍스트의 관리를 받아 업데이트 하는 것도 가능하다
     * dirtyChecking.
     * */

    /*주문 결과 페이지로 이동하려면 서비스 계층에서 식별자 orderId를 받아서 다시 다시 렌더링 하는 방법도 생각해 볼 수 있다.*/

    //@ModelAttribute가 있으면 담을 수도 있고 뿌릴 수도 있다.
    //뷰로부터 담아올 수도 있고, (모델에 굳이 안 담아도 자동으로 ) 모델을 넘기듯이 뷰로 넘겨서 데이터를 받아올 수도 있다.

    @GetMapping("/orders")
    public String list(@ModelAttribute OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel") //자바스크립트로 넘긴 post요청이 여기로 오면 파라미터orderId를 기준으로 취소한다,
    public String cancel(@PathVariable("orderId") Long orderId) {
        orderService.cancel(orderId);
        return "redirect:/orders"; //다시 orders로 리다이렉트.
    }
}
