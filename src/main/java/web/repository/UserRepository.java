package web.repository;

import web.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> find(Long id);

    User find(String email);

    void save(User entity);

    void delete(User entity);
}
