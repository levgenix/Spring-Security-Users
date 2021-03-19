package web.repository;

import org.springframework.stereotype.Repository;
import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // todo cache
    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    public Role findRoleByAuthority(String authority) throws NoSuchElementException {
        return findAll().stream()
                .filter(r -> authority.equals(r.getAuthority()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Role %s not found", authority)));
    }

//    @Override
//    public List<Role> createRoles(List<Role> roles) {
//
//        for (Role role : roles) {
//            sessionFactory.openSession().saveOrUpdate(role);
//            //System.out.println(id);
//            //role = entityManager.merge(role);
//        }
//        //entityManager.
//
//        return roles;
//    }
}
