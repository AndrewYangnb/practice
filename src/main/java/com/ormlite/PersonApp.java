package com.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;


import javax.swing.*;
import java.util.List;

public class PersonApp {
    private static final String databaseUrl = "jdbc:mysql://localhost/test?user=root&password=123456";
    private Dao<Person, Integer> personDao;


    public static void main(String[] args) throws Exception {
        new PersonApp().doMain(args);
    }

    public void doMain(String[] args) throws Exception {
        ConnectionSource connectionSource = null;
        int agePlus = 10;
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl);
            setUpDatabase(connectionSource);
//            readWriteData();
            printNames();
//            deleteObjects();
            grownUp(agePlus);
            System.out.println("keke");
        } finally {
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }
    private void setUpDatabase(ConnectionSource connectionSource) throws Exception {
        personDao = DaoManager.createDao(connectionSource, Person.class);
//        TableUtils.createTable(connectionSource, Person.class);
    }
    private void readWriteData() throws Exception {
        List<Integer> idList = List.of(0, 1, 2, 3, 4, 5, 6);
        List<String> nameList = List.of("Miki", "Groove", "sneak", "Rocky", "walter", "Peggy", "Brown");
        List<Integer> ageList = List.of(10, 20, 40, 60, 36, 35, 12);

        for (int i = 0; i < idList.size(); i++){
            Person person = new Person();
            person.setId(idList.get(i));
            person.setName(nameList.get(i));
            person.setAge(ageList.get(i));
            personDao.create(person);
        }
    }
    private void printNames() throws Exception {
        List<Person> persons = personDao.queryForAll();
        for (Person person : persons) {
            Integer age = person.getAge();
//            System.out.println(person.getName());
            System.out.println(person.getName() + "'s age is " + age);
            if (age >= 100) {
                System.out.println("Not bad！");
            }
        }
    }

    private void deleteObjects() throws Exception {
        List<Person> persons = personDao.queryForAll();
        for (Person person : persons) {
            personDao.delete(person);
        }
    }

    private void grownUp(Integer agePlus) throws Exception {
        List<Person> persons = personDao.queryForAll();
        for (Person person : persons) {
            int age = person.getAge();
            person.setAge(age + agePlus);
            personDao.update(person);
        }
//        List<Person> person = persons
//                .stream()
//                .forEach(e -> e.setAge(e.getAge() + agePlus))
//                .;
    }

}
