package com.ebook.multbooks.app.postkeyword.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class PostKeyword extends BaseEntity {
    private String content;
}
