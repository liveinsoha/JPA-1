package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    //Logger log = LoggerFactory.getLogger(getClass()); //@Slf4j어노테이션이 이 log를 만들어준다.

    @RequestMapping("/") //첫번째 화면이 매핑되었다. 이제 index.html대신 home.html이 렌더링 된다
    public String home() {
        log.info("home controller");
        return "home";
    }
}
