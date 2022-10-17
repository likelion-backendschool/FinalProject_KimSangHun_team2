package com.ebook.multbooks.app.member.repository;

import com.ebook.multbooks.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
