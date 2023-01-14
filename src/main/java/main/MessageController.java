package main;

import main.dto.DtoMessage;
import main.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MessageController {
    private final MessageService messageService;
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/message")
    public Map<String, String> sendMessage(@RequestParam String text, @RequestParam String name){
        return messageService.sendMessage(text, name);
    }
    @GetMapping("/message")
    public List<DtoMessage> getMessagesList(@RequestParam String time){
        return messageService.getMessages(time);
    }
}
