package com.ebook.multbooks.global.mapper;

import com.ebook.multbooks.app.post.dto.PostListDto;
import com.ebook.multbooks.app.post.dto.PostWriteForm;
import com.ebook.multbooks.app.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Post <-> PostDto 변환을 해주는
 * 자바 bean 매퍼
 *
 * interface를 생성하면 자동으로 구현 클래스를 생성해줌(generated 안에 구현클래스 생김)
 * */
@Mapper(componentModel = "spring")//componetModel=>객체 주입과정을 스프링 IOC 를 사용하겠다
public interface PostMapper {


    @Mapping(target = "nickname",source = "member.nickname")//연관관계인 컬럼과 연결
    PostListDto postToPostListDto(Post post);

    List<PostListDto> postsToPosListDtos(List<Post> posts);

    Post postWriteFormToPost(PostWriteForm postWriteForm);

}
