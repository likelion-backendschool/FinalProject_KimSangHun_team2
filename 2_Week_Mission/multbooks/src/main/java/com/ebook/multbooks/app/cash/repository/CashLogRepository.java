package com.ebook.multbooks.app.cash.repository;

import com.ebook.multbooks.app.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog,Long> {
}
