package services;

import org.hibernate.cfg.Configuration;
import rest.UserProfile;
import services.AccountService;

import java.util.Collection;

/**
 * Created by morev on 06.04.16.
 */
public class AccountServiceOnHibernate implements AccountService{
    private final Configuration configuration;

    AccountServiceOnHibernate() {
        configuration = new Configuration();

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/BloodyDefenceDB");
        configuration.setProperty("hibernate.connection.username", "bloodydefender");
        configuration.setProperty("hibernate.connection.password", "stargate");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
    }

    @Override
    public UserProfile getUser(long userID) {
        return null;
    }

    @Override
    public UserProfile getUser(String login) {
        return null;
    }

    @Override
    public long addUser(UserProfile user) {
        return 0;
    }

    @Override
    public boolean modifyUser(long userID, UserProfile user) {
        return false;
    }

    @Override
    public boolean deleteUser(long userID) {
        return false;
    }

    @Override
    public Collection<UserProfile> getAllUsers() {
        return null;
    }
}
