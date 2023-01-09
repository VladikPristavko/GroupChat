package main.dto;

import main.model.Message;

import java.time.format.DateTimeFormatter;

public class MessageMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    public static DtoMessage map(Message message){
        return new DtoMessage(
                message.getUser().getName(),
                message.getDateTime().format(FORMATTER),
                message.getText()
        );
    }
}
