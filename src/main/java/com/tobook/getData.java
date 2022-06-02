package com.tobook;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class getData {

    public static final Logger logger = LogManager.getLogger(getData.class.getName());

    public static void main(String[] args) throws Exception{
        String dataUrl = "jdbc:mysql://localhost:3306/book?user=root&password=123456";

        ConnectionSource connectionSource = new JdbcConnectionSource(dataUrl);

        Dao<Book,String> bookDao = DaoManager.createDao(connectionSource,Book.class);

        List<Book> books;

        books = bookDao.queryForAll();

        books.forEach(e -> logger.info(e.getId() + " " + e.getName() + " " + e.getAuthor()));

//        Integer book = books.l;
    }

}
