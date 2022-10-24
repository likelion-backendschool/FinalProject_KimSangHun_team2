package com.ebook.multbooks.app.postkeyword.repository;

import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getKeywordByMemberIdQsl(Long loginMemberId);
}
