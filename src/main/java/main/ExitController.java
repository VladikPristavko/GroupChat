package main;

import main.service.ExitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ExitController {
    private final ExitService exitService;
    @Autowired
    public ExitController(ExitService exitService) {
        this.exitService = exitService;
    }

    @PostMapping("/exit")
    public Map<String, String> exit(@RequestParam String name){
        return exitService.exit(name);
    }
}
