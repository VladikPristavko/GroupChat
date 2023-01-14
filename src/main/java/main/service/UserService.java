package main.service;

import main.model.User;
import main.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Long getCount(){
        return userRepository.count();
    }
    public List<String> getAll(){
        return userRepository.findAllBySessionIdNotLike("empty")
                .stream()
                .map(User:: getName)
                .collect(Collectors.toList());
    }
}
