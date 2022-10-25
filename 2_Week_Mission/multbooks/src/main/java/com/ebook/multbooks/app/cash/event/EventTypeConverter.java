package com.ebook.multbooks.app.cash.event;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class EventTypeConverter implements AttributeConverter<EventType,String> {

    @Override
    public String convertToDatabaseColumn(EventType eventType) {
        if(eventType==null){
            return null;
        }
        return eventType.getMessage();
    }

    @Override
    public EventType convertToEntityAttribute(String message) {
        if(message==null){
            return null;
        }
        return Stream.of(EventType.values())
                .filter(E->E.getMessage()==message)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
