package com.ebook.multbooks.app.posthashtag.repository;

import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.posthashtag.entity.PostHashTag;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostHashTagRepository extends JpaRepository<PostHashTag,Long> {

    List<PostHashTag> findByPost(Post post);

    @Transactional
    @Modifying
    @Query("delete from PostHashTag ph where ph.post = :post")
    void deleteByPost(@Param("post") Post post);

    Optional<PostHashTag> findByPostIdAndPostKeywordId(Long postId, Long keywordId);
}
