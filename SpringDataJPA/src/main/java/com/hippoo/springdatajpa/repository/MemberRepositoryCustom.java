package com.hippoo.springdatajpa.repository;

import com.hippoo.springdatajpa.entity.Member;
import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
