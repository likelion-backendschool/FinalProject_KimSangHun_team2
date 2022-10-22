package com.ebook.multbooks.app.post.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.post.dto.PostModifyForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Post extends BaseEntity {
    private String subject;
    private String content;
    private String contentHtml;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    public void updateAuthor(Member author) {
        this.author=author;
    }

    public void modify(PostModifyForm postModifyForm) {
        subject=postModifyForm.getSubject();
        content=postModifyForm.getContent();
        contentHtml=postModifyForm.getContentHtml();
    }
}
