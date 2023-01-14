package main;

import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users-count")
    public Long getUsersCount(){
        return userService.getCount();
    }
    @GetMapping("/users")
    public List<String> getUsersList(){
        return userService.getAll();
    }
}
