package com.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Example account object that is persisted to disk by the DAO and other example classes.
 */
@DatabaseTable(tableName = "accounts_0")
public class Account {

    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private String password;
    @DatabaseField
    private Integer age ;


    public Account() {
        // ORMLite needs a no-arg constructor
    }
    public Account(String name, String password, Integer age) {
        this.name = name;
        this.password = password;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

}