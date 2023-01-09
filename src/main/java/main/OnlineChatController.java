package main;

import main.dto.DtoMessage;
import main.dto.MessageMapper;
import main.model.Message;
import main.model.MessageRepository;
import main.model.User;
import main.model.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class OnlineChatController {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @GetMapping("/init")
    public Map<String, String> init(){
        Optional<User> user = userRepository.findBySessionId(
                RequestContextHolder.currentRequestAttributes().getSessionId());
        return user.map(value -> {
                    String enterChatTime = LocalDateTime.now().format(FORMATTER);
                    return Map.of(
                            "result", "ok",
                            "name", value.getName(),
                            "enter", enterChatTime
                    );
                }
        ).orElseGet(() -> Map.of("result", "new"));
    }
    @PostMapping("/auth")
    public Map<String, String> auth(@RequestParam String name, @RequestParam String password){
        if (Strings.isNotEmpty(name)) {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            String enterChatTime = LocalDateTime.now().format(FORMATTER);
            Optional<User> user = userRepository.findByName(name);
            if(user.isEmpty()) {
                userRepository.save(new User(
                        name,
                        sessionId,
                        password
                ));
                return Map.of(
                        "result", "ok",
                        "name", name,
                        "enter", enterChatTime
                );
            }else{
                User authUser = user.get();
                if(authUser.getPassword().equals(password)) {
                    authUser.setSessionId(sessionId);
                    userRepository.save(authUser);
                    return Map.of(
                            "result", "ok",
                            "name", name,
                            "enter", enterChatTime);
                } else {
                    return Map.of("result", "wrongPWD");
                }
            }
        } else{
            return Map.of("result", "empty");
        }
    }
    @PostMapping("/message")
    public Map<String, String> sendMessage(@RequestParam String text, @RequestParam String name){
        if (Strings.isEmpty(text)){
            return Map.of("result", "empty");
        }
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()) {
            messageRepository.save(new Message(
                    user.get(),
                    text
            ));
            return Map.of("result", "ok");
        } else {
            return Map.of("result", "notAuthorized");
        }
    }
    @GetMapping("/users-count")
    public Long getUsersCount(){
        return userRepository.count();
    }
    @GetMapping("/users")
    public List<String> getUsersList(){
       return userRepository.findAllBySessionIdNotLike("empty")
               .stream()
               .map(User :: getName)
               .collect(Collectors.toList());
    }
    @GetMapping("/message")
    public List<DtoMessage> getMessagesList(@RequestParam String time){
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
                .map(MessageMapper :: map)
                .collect(Collectors.toList());
    }
    @PostMapping("/exit")
    public Map<String, String> exit(@RequestParam String name){
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()){
            User exitUser = user.get();
            exitUser.setSessionId("empty");
            userRepository.save(exitUser);
            return Map.of("result", "ok");
        } else {
            return Map.of("result", "noUser");
        }
    }
}