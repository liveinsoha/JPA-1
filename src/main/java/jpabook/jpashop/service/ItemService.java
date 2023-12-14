package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //클래스 레벨 읽기 전용 트랜잭션
public class ItemService { //리포지토리에 역할을 단순 위임하는 서비스클래스이다 존재의미에 대해 생각해보자. -> 있어도 되고 없어도 되고.

    @Autowired
    private final ItemRepository itemRepository;

    @Transactional //쓰기 기능이 있는 메서드는 @트랜잭션 한번 더 걸어준다.
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
