package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import main.UserProfile;

import java.util.List;

/**
 * Created by morev on 06.04.16.
 */
public class AccountServiceOnHibernate implements AccountService{
    private static final Logger logger = LogManager.getLogger(AccountServiceOnHibernate.class);
    private final SessionFactory sessionFactory;

    public AccountServiceOnHibernate() {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/BloodyDefenceDB");
        configuration.setProperty("hibernate.connection.username", "bloodydefender");
        configuration.setProperty("hibernate.connection.password", "stargate");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        configuration.addAnnotatedClass(UserProfile.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public UserProfile getUser(long userID) {
        try (Session session = sessionFactory.openSession()) {
            UserDAO userDAO = new UserDAO(session);
            return userDAO.getUser(userID);
        }
    }

    @Override
    public UserProfile getUser(String login) {
        try (Session session = sessionFactory.openSession()) {
            UserDAO userDAO = new UserDAO(session);
            return userDAO.getUser(login);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            UserDAO userDAO = new UserDAO(session);
            return userDAO.getAllUsers();
        }
    }

    @Override
    public long addUser(UserProfile user) {
        try (Session session = sessionFactory.openSession()) {
            UserDAO userDAO = new UserDAO(session);
            if (userDAO.getUser(user.getLogin()) == null) {
                userDAO.addUser(user);
                return userDAO.getUser(user.getLogin()).getId();
            } else {
                return -1;
            }
        }
    }

    @Override
    public boolean modifyUser(long userID, UserProfile user) {
        try (Session session = sessionFactory.openSession()) {
            user.setId(userID);
            UserDAO userDAO = new UserDAO(session);
            UserProfile oldUser = userDAO.getUser(user.getId());
            if (oldUser == null) {
                return false;
            }
            if (! user.getLogin().equals(oldUser.getLogin()))
                return false;
            userDAO.modifyUser(userID, user);
            return true;
        }
    }

    @Override
    public boolean deleteUser(long userID) {
        try (Session session = sessionFactory.openSession()) {
            UserDAO userDAO = new UserDAO(session);
            if (userDAO.getUser(userID) != null) {
                userDAO.deleteUser(userID);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isConnected() {
        try (Session session = sessionFactory.openSession()) {
            session.isConnected();
            session.createSQLQuery("SELECT 1").list();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }
}
