package main.service;

import main.model.User;
import main.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ExitService {
    private final UserRepository userRepository;
    @Autowired
    public ExitService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Map<String, String> exit(String name){
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
