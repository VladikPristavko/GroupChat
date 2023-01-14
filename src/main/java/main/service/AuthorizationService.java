package main.service;

import main.model.User;
import main.model.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthorizationService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    private String enterChatTime;
    private String sessionId;
    private final UserRepository userRepository;
    @Autowired
    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Map<String, String> authorize(){
        sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        enterChatTime = LocalDateTime.now().format(FORMATTER);
        Optional<User> user = userRepository.findBySessionId(sessionId);
        return user.map(value -> Map.of(
                "result", "ok",
                "name", value.getName(),
                "enter", enterChatTime
        )
        ).orElseGet(() -> Map.of("result", "new"));
    }
    public Map<String, String> register(String name, String password){
        if (Strings.isNotEmpty(name)) {
            Optional<User> user = userRepository.findByName(name);
            return user.isEmpty() ? createUser(name, password)
                    : updateUsersSessionId(name, password, user);
        } else{
            return Map.of("result", "empty");
        }
    }

    private Map<String, String> createUser(String name, String password){
        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setSessionId(sessionId);
        userRepository.save(newUser);
        return Map.of(
                "result", "ok",
                "name", name,
                "enter", enterChatTime
        );
    }
    private Map<String, String> updateUsersSessionId(String name, String password, Optional<User> user){
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
}
