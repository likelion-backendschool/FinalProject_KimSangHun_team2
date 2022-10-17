package com.ebook.multbooks.app.base.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(updatable = false)//개발자가 임의로 수정 못하도록 false 설정
    private LocalDateTime updateDate;

}
