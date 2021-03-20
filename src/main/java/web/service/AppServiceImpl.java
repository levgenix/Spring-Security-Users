package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;
import web.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AppServiceImpl implements AppService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUser(Long id) {
        return userRepository.find(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(String.format("User with ID = %d not found", id), 1));
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.find(email);
        if (null == user) {
            throw new UsernameNotFoundException(String.format("User email %s not found", email));
        }
        return user;
    }

    @Override
    @Transactional
    public void saveOrUpdateUser(User user) {
        for (Role role : user.getRoles()) {
            try {
                role.setId(roleRepository.findRoleByAuthority(role.getAuthority()).getId());
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    //@PostConstruct TODO
    private void populateUsers() {

    }
}
