package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;
import web.repository.UserRepository;

import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AppServiceImpl implements AppService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AppServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(long userId) throws NullPointerException {
        User user = userRepository.findUser(userId);
        if (null == user) {
            System.out.printf("User with id = %d not found", userId);
            throw new NullPointerException(String.format("User with id = %d not found", userId));
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUser(email);
        if (null == user) {
            System.out.printf("User email %s not found", email);
            throw new UsernameNotFoundException(String.format("User email %s not found", email));
        }
        return user;
    }

    @Override
    public void createOrUpdateUser(User user) throws PersistenceException {
        System.out.println(user);
        for (Role role : user.getRoles()) {
            try {
                role.setId(roleRepository.findRoleByAuthority(role.getAuthority()).getId());
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
        System.out.println(user);
        if (0 == user.getId()) {
            createUser(user);
        } else {
            updateUser(user);
        }
    }

    private void createUser(User user)/* throws PersistenceException*/ {
        // TODO
        // Request processing failed; nested exception is javax.persistence.PersistenceException: org.hibernate.exception.ConstraintViolationException: could not execute statement

        // Root cause
        // java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '1@1' for key 'users.UK_6dotkott2kjsp8vw4d0m25fb7'
        // ...
        // 	web.repository.UserRepositoryImpl.createUser(UserRepositoryImpl.java:55)
        //	web.service.UserServiceimpl.createUser(UserServiceimpl.java:63)
        //	web.service.UserServiceimpl.createOrUpdateUser(UserServiceimpl.java:56)

        userRepository.createUser(user);
    }

    private void updateUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }


    // TODO
    //@PostConstruct
    private void populateUsers() {

//        List<Role> roles = new ArrayList<>();
//        roles.add(new Role("ROLE_ADMIN"));
//        roles.add(new Role("ROLE_USER"));
//        userRepository.createRoles(roles);
//
//        for (Role role : roles) {
//            System.out.println(role);
//        }

//        for (Role role : userRepository.getAllRoles()) {
//            System.out.println(role);
//        }

        User admin = new User("Ivan", "Ivanov", "admin@mail.ru", "admin12");
        admin.setRoles(new HashSet<Role>(){{
            add(new Role("ROLE_ADMIN"));
            add(new Role("ROLE_USER"));
        }});
        userRepository.createUser(admin);

        User user = new User("Vasya", "Petrov", "user@mail.ru", "user12");
        user.setRoles(new HashSet<Role>(){{
            add(new Role("ROLE_USER"));
        }});
        userRepository.createUser(user);

        //Set<Role> user_roles = new HashSet<>({userRepository.getAllRoles()});
//        user.setRoles(new Set<Role>() {
//        });
//
//        Set<Role> roles = new HashSet<>();
//        roles.add(new Role("ROLE_USER"));
////        roles.add(new Role(2, "ROLE_ADMIN"));
//        user.setRoles(roles);
    }
}
