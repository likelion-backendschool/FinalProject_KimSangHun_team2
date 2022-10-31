package com.ebook.multbooks.app.post.repository;

import com.ebook.multbooks.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    //수정날짜 순으로 모든 게시물 가져오기
    @Query("select p from Post p ORDER BY p.updateDate")
    List<Post> getAllPostsOrderByUpdateDate();
}
