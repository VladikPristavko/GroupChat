package main.service;

import main.dto.DtoMessage;
import main.dto.MessageMapper;
import main.model.Message;
import main.model.MessageRepository;
import main.model.User;
import main.model.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    @Autowired
    public MessageService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public Map<String, String> sendMessage(String text, String name){
        if (Strings.isEmpty(text)){
            return Map.of("result", "empty");
        }
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()) {
            Message newMessage = new Message();
            newMessage.setText(text);
            newMessage.setUser(user.get());
            newMessage.setDateTime(LocalDateTime.now());
            messageRepository.save(newMessage);
            return Map.of("result", "ok");
        } else {
            return Map.of("result", "notAuthorized");
        }
    }

    public List<DtoMessage> getMessages(String time){
        LocalDateTime lastUpdate = Strings.isEmpty(time) ?
                LocalDateTime.now().minusSeconds(5) :
                LocalDateTime.parse(
                        time.replace(".", "-")
                                .replace(" ", "T")
                );
        return messageRepository
                .findBydateTimeAfter(
                        lastUpdate,
                        Sort.by(Sort.Direction.ASC, "dateTime")
                )
                .stream()
                .map(MessageMapper:: map)
                .collect(Collectors.toList());
    }
}
