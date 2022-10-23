package com.ebook.multbooks.product;

import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.global.rq.Rq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.ReferenceQueue;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles({"test","secret"})
public class ProductServiceTest {
    @Autowired
    private PostKeywordService postKeywordService;
    @Autowired
    private Rq rq;
    @Test
    @DisplayName("getKeywordByMemberId 테스트")
    @WithUserDetails("user1")
    public void t1(){
        List<PostKeyword> postKeywords=postKeywordService.getKeywordByMemberId(rq.getId());
        assertThat(postKeywords.size()).isGreaterThanOrEqualTo(2);
    }
}
