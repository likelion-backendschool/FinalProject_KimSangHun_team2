package com.ebook.multbooks.app.postkeyword.repository;

import com.ebook.multbooks.app.posthashtag.entity.QPostHashTag;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.entity.QPostKeyword;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.ebook.multbooks.app.posthashtag.entity.QPostHashTag.postHashTag;
import static com.ebook.multbooks.app.postkeyword.entity.QPostKeyword.postKeyword;

@Repository
@RequiredArgsConstructor
public class PostKeywordRepositoryImpl implements PostKeywordRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<PostKeyword> getKeywordByMemberIdQsl(Long loginMemberId) {
       List<Tuple> fetch=jpaQueryFactory
               .select(postKeyword, postHashTag.count())
               .from(postKeyword)
               .innerJoin(postHashTag)
               .on(postKeyword.eq(postHashTag.postKeyword))
               .where(postHashTag.member.id.eq(loginMemberId))
               .orderBy(postHashTag.post.id.desc())
               .groupBy(postKeyword.id)
               .fetch();
       return fetch.stream()
               .map(tuple -> {
                   PostKeyword _postKeyword=tuple.get(postKeyword);
                   Long postHashTagCount=tuple.get(postHashTag.count());

                   _postKeyword.getExtra().put("postHashTagCount",postHashTagCount);
                   return _postKeyword;
               })
               .collect(Collectors.toList());
    }
}
