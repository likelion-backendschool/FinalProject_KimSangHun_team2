package com.ebook.multbooks.app.post.repository;

import com.ebook.multbooks.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
