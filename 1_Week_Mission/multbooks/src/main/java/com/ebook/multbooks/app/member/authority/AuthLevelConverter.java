package com.ebook.multbooks.app.member.authority;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Converter;
import java.util.stream.Stream;
/**
 * JPA 에서 지원하는
 * enum->값
 *값->enum 을
 * db 접근 전후에
 * 지원하는 interface 구현
 * */
@Converter(autoApply = true)
public class AuthLevelConverter implements AttributeConverter <AuthLevel,Integer> {

    // db 저장전에 enum 을 value 값으로 변환해서 넣어줌
    @Override
    public Integer convertToDatabaseColumn(AuthLevel authLevel) {

        if(authLevel==null){
            return null;
        }

        return authLevel.getLevel();
    }

    //db 에서 호출시 value 값을 enum 으로 변경
    @Override
    public AuthLevel convertToEntityAttribute(Integer dbLevel) {

       if(dbLevel==null){
           return null;
       }
    //enum 안의 모든 값의 value 값을 비교해서 일치하는 enum 을 반환
       return Stream.of(AuthLevel.values())
               .filter(l->l.getLevel()==dbLevel)
               .findFirst()
               .orElseThrow(IllegalArgumentException::new);
    }
}
