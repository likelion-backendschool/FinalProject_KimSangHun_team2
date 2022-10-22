package com.ebook.multbooks.app.postkeyword.repository;

import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostKeywordRepository extends JpaRepository<PostKeyword,Long> {
    Optional<PostKeyword> findByContent(String content);
}
