package com.ormlite;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.checkerframework.checker.units.qual.A;

import java.sql.SQLException;
import java.util.List;


public class AccountApp {

    public static void main(String[] args) throws Exception {

//        Class.forName("com.mysql.jdbc.Driver");

        // this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:mysql://localhost/test?user=root&password=123456";
        // create a connection source to our database
        ConnectionSource connectionSource =
                new JdbcConnectionSource(databaseUrl);

        // instantiate the dao
        Dao<Account, String> accountDao =
                DaoManager.createDao(connectionSource, Account.class);

        // if you need to create the 'accounts' table make this call
//        TableUtils.createTable(connectionSource, Account.class);

        // create an instance of Account
//        String name = "Jim";
        Account account = new Account();
//        Account account_update = accountDao.queryForId("Hei");
//        account.setName("Jhon");
//        account.setPassword("3.14");
//        account.setAge(18);
//        account_update.setAge(19);

//        accountDao.deleteById("Lerry");

        // persist the account object to the database
//        accountDao.create(account);
//        accountDao.update(account_update);
        accountDao.deleteById("Jay");

        // retrieve the account from the database by its id field (name)
        Account account2 = accountDao.queryForId("Jim");
        List<Account> accounts = accountDao.queryForAll();
        for (Account i : accounts) {
            System.out.println("Name " + i.getName());
        }
//        System.out.println("Account: " + account2.getName());

        // close the connection source
        connectionSource.close();
    }
}