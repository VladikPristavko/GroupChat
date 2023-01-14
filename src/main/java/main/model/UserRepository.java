package main.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findBySessionId(String sessionId);
    Optional<User> findByName(String name);
    List<User> findAllBySessionIdNotLike(String sessionId);

}
