package com.hippoo.springdatajpa.repository;

import com.hippoo.springdatajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
