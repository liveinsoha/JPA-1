package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("B")
public class Book extends Item {

    public Book() {

    }

    public Book(String name, int price, int stockQuantity, String author, String isbn) {
        super.name = name;
        super.price = price;
        super.stockQuantity = stockQuantity;
        this.author = author;
        this.isbn = isbn;
    }

    private String author;
    private String isbn;

}
