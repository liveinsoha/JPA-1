package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = new Book();
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String createEditForm(@PathVariable("itemId") Long itemId, Model model) {
        Book book = (Book) itemService.findOne(itemId); //수정일 경우 itemId가 뷰에서 넘어온다.
        BookForm bookForm = new BookForm(); //영속 엔티티를 꺼내어 폼의 필드에 주입하고 다시 뷰로 간다.
        //뷰에는 텍스트로 수정하기 전의 필드의 값들이 전시된다.

        bookForm.setId(book.getId());
        bookForm.setName(book.getName());
        bookForm.setPrice(book.getPrice());
        bookForm.setStockQuantity(book.getStockQuantity());
        bookForm.setAuthor(book.getAuthor());
        bookForm.setIsbn(book.getIsbn());

        model.addAttribute("form", bookForm);//bookForm을 model에 담아 객체로 넘긴다
        return "items/updateItemForm"; //이 html파일에서 수정되기 전의 값을 뿌린다.

    }

    /*위의 모델에서 attributeName에 담아 뷰로 보내는 form과 아래에서 @ModelAttribute로 받을 "form"이 동일해야 한다.
    * 두 form 모두 같은 뷰(items/updateItemForm) 출신이기 때문 */

    @PostMapping("/items/{itemId}/edit")
    public String update(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm bookForm) {
        //form객체가 자동으로 bookForm에 바인딩 된다? bookForm객체를 이용한 서버 로직 수행
        Book book = new Book(); //얘는 준영속 엔티티. 새로 생성된 객체라 영속성컨텍스트가 관리하진 않지만, id값이 세팅되어 있으므로 준영속 엔티티. DB에 함 들어갔다 나온 에티티로 취급한다.
        book.setId(bookForm.getId());
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn()); //여기서 어설프게 Book객체를 만드는 것은 좋지 않다. 파라미터로 넘기기.

        //itemService.saveItem(book);  // merge를 통한다.
        itemService.updateItem(book);
        return "redirect:/items";
    }


}
