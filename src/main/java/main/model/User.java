package main.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String sessionId;
    private String name;
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Message> messageList = new ArrayList<>();
}
