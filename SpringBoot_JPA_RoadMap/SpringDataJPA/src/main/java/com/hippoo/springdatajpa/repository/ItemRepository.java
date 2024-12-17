package com.hippoo.springdatajpa.repository;

import com.hippoo.springdatajpa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
