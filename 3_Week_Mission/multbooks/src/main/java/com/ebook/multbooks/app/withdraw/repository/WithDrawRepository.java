package com.ebook.multbooks.app.withdraw.repository;

import com.ebook.multbooks.app.withdraw.entity.WithDraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithDrawRepository extends JpaRepository<WithDraw,Long> {
}
