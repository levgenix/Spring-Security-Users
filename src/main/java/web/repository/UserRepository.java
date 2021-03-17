package web.repository;

import web.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAllUsers();

    User findUser(long userId);

    User findUser(String email);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(long userId);
}
