package services;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import main.UserProfile;

import java.util.List;

/**
 * Created by morev on 06.04.16.
 */
public class UserDAO {
    private final Session session;

    UserDAO(Session session) {
        this.session = session;
    }

    public UserProfile getUser(long id) {
        return session.get(UserProfile.class, id);
    }

    public UserProfile getUser(String login) {
        Criteria criteria = session.createCriteria(UserProfile.class);
        return (UserProfile) criteria
                .add(Restrictions.eq("login", login))
                .uniqueResult();
    }


    public List getAllUsers() {
        return session.createCriteria(UserProfile.class).list();
    }

    public void addUser(UserProfile user) {
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
    }

    public void modifyUser(long id, UserProfile user) {
        Transaction transaction = session.beginTransaction();
        UserProfile oldUser = session.load(UserProfile.class, id);
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        session.flush();
        transaction.commit();
    }

    public void deleteUser(long id) {
        Transaction transaction = session.beginTransaction();
        UserProfile user = session.load(UserProfile.class, id);
        session.delete(user);
        transaction.commit();
    }
}
