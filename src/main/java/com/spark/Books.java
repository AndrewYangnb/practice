package com.spark;
import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Books {
    private static final Map<String, Book> books = new HashMap<>();

    public static void main(String[] args) {
        final Random random = new Random();

        post("/books", (request, response) -> {
            String author = request.queryParams("author");
            String title = request.queryParams("title");

            Book book = new Book(author, title);

            int id = random.nextInt(Integer.MAX_VALUE);
            books.put(String.valueOf(id), book);

            response.status(201);
            return id;
        });

        get("/books/:id", (request, response) -> {
            Book book = books.get(request.params(":id"));

            if ( book != null) {
                return "Title: " + book.getTitle() + "  " + "Author: " + book.getAuthor();
            } else {
                response.status(404);
                return "Book not found";
            }
        });

        put("/books/:id", (request, response) -> {
            String id = request.params(":id");
            Book book = books.get(id);
            String newAuthor = request.queryParams("author");
            String newTitle = request.queryParams("title");
            if (book != null) {
                book.setAuthor(newAuthor);
                book.setTitle(newTitle);
                return  "Book with id '" + id + "' updated";
            } else {
                response.status(404);
                return "Book is not found";
            }
        });

        delete("/books/:id", (request, response) -> {
            String id = request.params(":id");
            Book book = books.remove(id);
            if (book != null) {
                return "Book with id '" + id + "' is deleted";
            } else {
                response.status(404);
                return "Book is not found";
            }
        });

        get("/books", (request, response) -> {
            StringBuilder ids = new StringBuilder();
            for (String id : books.keySet()) {
                ids.append(id).append(" ");
            }
            return ids.toString();
        });

    }

    public static class Book {
        String author;
        String title;

        public Book(String author, String title) {
            this.author = author;
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public String getTitle() {
            return title;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
