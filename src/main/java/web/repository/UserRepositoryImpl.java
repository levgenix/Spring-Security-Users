package web.repository;

import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllUsers() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public User findUser(long userId) {
        return entityManager.find(User.class, userId);
    }

    @Override
    public User findUser(String email) {
        /*TypedQuery<User> query = sessionFactory.openSession().createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        return query.getSingleResult();*/

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> itemRoot = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(itemRoot.get("email"), email));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public void createUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(long userId) {
        entityManager.remove(findUser(userId));
    }

//    public void populateUsers(List<User> users) {
//        sessionFactory.openSession().createQuery("TRUNCATE TABLE ");
//        sessionFactory.getCurrentSession().get(Role.class, 1);
//
//        Query query = sessionFactory.getCurrentSession().createQuery("delete from User where id = :userId");
//        query.setParameter("userId", 1).executeUpdate();
//    }
}
