package main;

import main.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AuthorizationController {
    private final AuthorizationService authorizationService;
    @Autowired
    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
    @GetMapping("/auth")
    public Map<String, String> authorize(){
        return authorizationService.authorize();
    }
    @PostMapping("/reg")
    public Map<String, String> register(@RequestParam String name, @RequestParam String password){
        return authorizationService.register(name, password);
    }
}