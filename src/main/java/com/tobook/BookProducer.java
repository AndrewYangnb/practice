package com.tobook;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;


import java.util.ArrayList;
import java.util.List;


import static spark.Spark.*;

public class BookProducer {
    public static final Logger logger = LogManager.getLogger(BookProducer.class.getName());
    private static Dao<Book, String> bookDao;
    static final String databaseUrl = "jdbc:mysql://localhost:3306/book?user=root&password=123456";

    public static void main(String[] args) throws Exception {
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);

//        Config config = new Config();
//        config.useSingleServer()
//                .setAddress("redis://localhost/6379")
//                .setPassword("123456");
//        RedissonClient redisson = Redisson.create(config);

        logger.info("Connecting...");
        setUp(connectionSource);
//        port(10000);
        logger.info("Connected, start working!");

        post("/book", (request, response) -> {
            String name = request.queryParams("name");
            String author = request.queryParams("author");
            Book book = new Book(name, author);
//            if (bookDao.)
            bookDao.createIfNotExists(book);

//            String id = bookDao.extractId(book);

            response.status(201);
            logger.info("This option is ok.");

            List<Book> books;
            books = bookDao.queryForAll();

            int index = books.size();

            //   需要重新进入数据库
            return "Your book's id = " + books.get(index - 1).getId();
        });

        get("/book", (request, response) -> "May i help you, sir.");

        get("/book/:id", (request, response) -> {
            String id = request.params(":id");
            Book book = bookDao.queryForId(id);
            if (book == null) {
                response.status(404);
                logger.info("This option is ok.");
                return "This book is not found";
            } else {
                response.status(201);
                logger.info("This option is ok.");
                return  "id: "+ id +" BookName: " + book.getName() + " " + "author: " + book.getAuthor();
            }
        });

        delete("/book/:id", (request, response) -> {
            String id = request.params("id");
            Book book = bookDao.queryForId(id);
            if (book != null) {
                bookDao.delete(book);
                response.status(201);
                logger.info("This option is ok.");
                return "OK";
            } else {
                response.status(404);
                logger.info("This option is ok.");
                return "This book is not found.";
            }
        });

    }
    public static void setUp(ConnectionSource connectionSource) throws Exception {
        bookDao = DaoManager.createDao(connectionSource, Book.class);
//        TableUtils.createTable(connectionSource, Book.class);
    }
}
