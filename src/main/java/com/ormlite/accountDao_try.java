package com.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class accountDao_try {

    public static void main(String[] args) throws Exception {

        String databaseUrl = "jdbc:mysql://localhost/test?user=root&password=123456";
        ConnectionSource connectionSource =
                new JdbcConnectionSource(databaseUrl);

        Dao<Person, String> personDao = DaoManager.createDao(connectionSource, Person.class);



    }

}
