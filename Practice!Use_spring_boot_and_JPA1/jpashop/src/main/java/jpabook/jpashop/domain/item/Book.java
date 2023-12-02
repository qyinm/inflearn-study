package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder
public class Book extends Item {

    private String author;
    private String isbn;
}
