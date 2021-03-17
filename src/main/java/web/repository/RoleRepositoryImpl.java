package web.repository;

import org.springframework.stereotype.Repository;
import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

//    private final SessionFactory sessionFactory;
//
//    @Autowired
//    public RoleRepositoryImpl(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }

    @Override
    public List<Role> findAll() {
//        TypedQuery<Role> query = sessionFactory.openSession().createQuery("from Role", Role.class);
//        return query.getResultList();
        return entityManager.createQuery("from Role", Role.class).getResultList();
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
