package com.ebook.multbooks.app.base.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  공통적인 속성인
 *  ID,생성날짜,수정날짜 를 제공해주는 Entity
 * */
@Getter
@SuperBuilder
@MappedSuperclass//단순히 속성값만전달하는 클래스 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)//DB에 적용하기전,후에 커스텀콜백 요청가능
@ToString
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)//개발자가 임의로 수정 못하도록 false 설정
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Transient // 아래 필드가 DB 필드가 되는 것을 막는다.
    @Builder.Default
    // 이 필드 덕분에 다양한 DTO 클래스를 만들 필요성이 줄어들게 된다.
    // 하지만 이 방식은 DTO 방식에 비해서 휴먼에러가 일어날 확률을 높힌다.
    // 그것은 TDD로 보완 해야 한다.
    private Map<String, Object> extra = new LinkedHashMap<>();

}
