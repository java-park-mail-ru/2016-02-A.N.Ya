package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(AccountServiceOnHibernate.class);
    private final Session session;

    UserDAO(Session session) {
        this.session = session;
    }

    public UserProfile getUser(long id) {
        try {
            return session.get(UserProfile.class, id);
        } catch (Exception e) {
            logger.error("Database failed ", e);
            return null;
        }
    }

    public UserProfile getUser(String login) {
        try {
            Criteria criteria = session.createCriteria(UserProfile.class);
            return (UserProfile) criteria
                    .add(Restrictions.eq("login", login))
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Database failed ", e);
            return null;
        }


    }


    public List getAllUsers() {
        return session.createCriteria(UserProfile.class).list();
    }

    public void addUser(UserProfile user) {
        try {
            session.getTransaction().begin();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Database failed ", e);
        } finally {
            session.close();
        }

    }

    public void modifyUser(long id, UserProfile user) {
        try {
            session.getTransaction().begin();
            UserProfile oldUser = session.load(UserProfile.class, id);
            oldUser.setPassword(user.getPassword());
            oldUser.setEmail(user.getEmail());
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Database failed ", e);
        } finally {
            session.close();
        }
    }

    public void deleteUser(long id) {
        try {
            UserProfile user = session.load(UserProfile.class, id);
            session.delete(user);
        } catch (Exception e) {
            logger.error("Database failed ", e);
        } finally {
            session.close();
        }

    }
}
