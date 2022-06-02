package com.tobook;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "books")
public class Book {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String author;

    public Book() {

    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }
    public  void  setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
