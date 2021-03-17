package web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import web.model.Role;
import web.model.User;

import java.util.List;

public interface AppService extends UserDetailsService {
    List<User> findAllUsers();

    User findUser(long userId) throws NullPointerException;

    void createOrUpdateUser(User user);

    void deleteUser(long userId);

    List<Role> findAllRoles();
}
