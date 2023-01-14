package main.dto;

import main.model.Message;

import java.time.format.DateTimeFormatter;

public class MessageMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    public static DtoMessage map(Message message){
        DtoMessage dtoMessage = new DtoMessage();
        dtoMessage.setUsername(message.getUser().getName());
        dtoMessage.setText(message.getText());
        dtoMessage.setDatetime(message.getDateTime().format(FORMATTER));
        return dtoMessage;
    }
}
