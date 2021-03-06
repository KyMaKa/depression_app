package dbService;

import accountService.UserAccount;

import configurator.Configurator;
import configurator.ConfiguratorException;
import dbService.dataSets.*;
import dbService.dao.*;


import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

/**
 * Utility class to handle data base manipulations.
 * @author Tesorp1X
 */
public class DBService {

    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "none";

    private final SessionFactory sessionFactory;

    public DBService() throws ConfiguratorException {

        Configuration configuration = getMySqlConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private Configuration getMySqlConfiguration() throws ConfiguratorException {

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(NoteDataSet.class);

        Configurator configurator = new Configurator("mysql.conf");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", configurator.getUrl());
        configuration.setProperty("hibernate.connection.username", configurator.getUser());
        configuration.setProperty("hibernate.connection.password", configurator.getPassword());

        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);

        return configuration;
    }


    /*       User manipulation      */


    public long addUser(String username, String password) {

        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserDAO userDAO = new UserDAO(session);
            long id = userDAO.addNewUser(username, password);
            transaction.commit();
            session.close();

            return id;

        } catch (HibernateException e) {

            return -1;
        }

    }

    public long addUser(String username, String password, String telegram) {

        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserDAO userDAO = new UserDAO(session);
            long id = userDAO.addNewUser(username, password, telegram);
            transaction.commit();
            session.close();

            return id;

        } catch (HibernateException e) {

            return -1;
        }

    }

    public void deleteUserByUsername(String username) throws NoSuchUserException {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserDAO userDAO = new UserDAO(session);
        UserDataSet user_to_delete = userDAO.getUserByUsername(username);

        boolean is_deleted = userDAO.deleteUserById(user_to_delete.getId());
        transaction.commit();
        session.close();

        if (!is_deleted) {

            throw new NoSuchUserException(username);
        }
        //TODO: LOG INFO ABOUT DELETION.
    }

    public void deleteUserByTelegram(String telegram) throws NoSuchUserException {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserDAO userDAO = new UserDAO(session);
        UserDataSet user_to_delete = userDAO.getUserByTelegram(telegram);

        boolean is_deleted = userDAO.deleteUserById(user_to_delete.getId());
        transaction.commit();
        session.close();
        if (!is_deleted) {

            throw new NoSuchUserException(telegram);
        }
        //TODO: LOG INFO ABOUT DELETION.
    }

    public void updateUser(String username, String new_password, String new_telegram) throws NoSuchUserException {

        UserDataSet user_to_update = findUserByUsername(username);
        user_to_update.setPassword(new_password);
        user_to_update.setTelegram(new_telegram);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserDAO userDAO = new UserDAO(session);
        userDAO.updateUser(user_to_update);

        //TODO: LOG INFO ABOUT UPDATE.
        transaction.commit();
        session.close();

    }

    public List<UserDataSet> getListOfUsers() {

        Session session = sessionFactory.openSession();

        UserDAO userDAO = new UserDAO(session);
        List<UserDataSet> result_list = userDAO.getListOfUsers();

        session.close();

        return  result_list;
    }

    public List<UserDataSet> getListOfUsers(int start_point, int max_result) {

        Session session = sessionFactory.openSession();

        UserDAO userDAO = new UserDAO(session);
        List<UserDataSet> result_list = userDAO.getListOfUsers(start_point, max_result);

        session.close();

        return  result_list;
    }

    public UserDataSet findUserByUsername(String username) throws NoSuchUserException {

        Session session = sessionFactory.openSession();
        UserDAO userDAO = new UserDAO(session);

        UserDataSet dataSet = userDAO.getUserByUsername(username);

        session.close();

        if (dataSet == null) {
            throw new NoSuchUserException(username);
        }

        return dataSet;
    }

    public UserDataSet findUserByTelegram(String telegram) throws NoSuchUserException {

        Session session = sessionFactory.openSession();
        UserDAO userDAO = new UserDAO(session);

        UserDataSet dataSet = userDAO.getUserByTelegram(telegram);

        session.close();

        if (dataSet == null) {
            throw new NoSuchUserException(telegram);
        }

        return dataSet;
    }


    public UserAccount getUserAccountByUsername(String username) throws NoSuchUserException {

        UserDataSet dataSet = findUserByUsername(username);

        return new UserAccount(dataSet.getId() ,dataSet.getUsername(), dataSet.getPassword(), dataSet.getTelegram());
    }

    public UserAccount getUserAccountByTelegram(String telegram) throws NoSuchUserException {

        UserDataSet dataSet = findUserByTelegram(telegram);

        return new UserAccount(dataSet.getId() ,dataSet.getUsername(), dataSet.getPassword(), dataSet.getTelegram());
    }

    //Note manipulation




}
