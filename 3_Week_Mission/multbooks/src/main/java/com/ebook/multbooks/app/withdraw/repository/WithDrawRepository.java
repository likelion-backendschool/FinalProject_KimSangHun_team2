package com.ebook.multbooks.app.withdraw.repository;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.withdraw.entity.WithDraw;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WithDrawRepository extends JpaRepository<WithDraw,Long> {

    Optional<WithDraw> findFirstByMemberOrderByCreateDateDesc(Member member);
    @Query("select w from WithDraw w where w.member= :member Order by w.createDate DESC")
    List<WithDraw> getMyWithDrawList(@Param("member") Member member);
}
