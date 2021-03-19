package web.repository;

import web.model.Role;

import java.util.List;
import java.util.NoSuchElementException;

public interface RoleRepository {
    List<Role> findAll();

    Role findRoleByAuthority(String authority) throws NoSuchElementException;
}
