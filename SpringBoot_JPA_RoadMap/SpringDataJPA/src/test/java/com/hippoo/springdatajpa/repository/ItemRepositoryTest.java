package com.hippoo.springdatajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.hippoo.springdatajpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    void save() {
        // Given
        Item item = new Item("A");
        itemRepository.save(item);
        // When

        // Then
    }

}