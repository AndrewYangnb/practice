package com.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ForeignMain {

    // 数据库url
    private static final String DATABASE_URL = "jdbc:mysql://localhost/test?user=root&password=123456";

    // 预定义Dao
    private Dao<Account_Foreign, Integer> accountDao;
    private Dao<Order, Integer> orderDao;

    public static void main(String[] args) throws Exception {

        new ForeignMain().doMain(args);
    }

    private void doMain(String[] args) throws Exception {
        // 预定义connectionSource
        JdbcConnectionSource connectionSource = null;

        try {
            // 创建connectionSource
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            setupDatabase(connectionSource);
            readWriteData();


        } finally {
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    /**
     * 用来启动数据库连接
     * @param connectionSource
     * @throws SQLException
     */
    private void setupDatabase(ConnectionSource connectionSource) throws SQLException {
        // 创建Dao
        accountDao = DaoManager.createDao(connectionSource, Account_Foreign.class);
        orderDao = DaoManager.createDao(connectionSource, Order.class);

        // 创建Table，第一次运行需创建
//        TableUtils.createTable(connectionSource, Account_Foreign.class);
//        TableUtils.createTable(connectionSource, Order.class);
    }

    private void readWriteData() throws Exception {
        // 实例化Account_Foreign 对象
        String name1 = "Dear Marry";
        Account_Foreign account1 = new Account_Foreign(name1);

        String name2 = "Jimmy";
        Account_Foreign account2 = new Account_Foreign(name2);

        // 将此对象持久化到数据库
        accountDao.create(account1);
        accountDao.create(account2);

        // 为Account创建关联的Order对象
        // Marry买了两个编号为 #21312 的物品，一个物品的价格是 12.32
        int quantity1 = 2;
        int itemNumber1 = 21312;
        float price1 = 12.32F;
        Order order1 = new Order(account1, itemNumber1, price1, quantity1);
        orderDao.create(order1);

        // Marry 又买了 1 个编号为 #785 的物品，一个物品的价格是 7.98
        int quantity2 = 1;
        int itemNumber2 = 785;
        float price2 = 7.98F;
        Order order2 = new Order(account1, itemNumber2, price2, quantity2);
        orderDao.create(order2);

        int quantity3 = 1;
        int itemNumber3 = 25535;
        float price3 = 10.1F;
        Order order3 = new Order(account2, itemNumber3, price3, quantity3);
        orderDao.create(order3);

        // 使用QueryBuilder构造查询
        QueryBuilder<Order, Integer> statementBuilder = orderDao.queryBuilder();
        // 找到与此账户匹配的所有订单
        statementBuilder.where().eq(Order.ACCOUNT_ID_FIELD_NAME, account1);

        List<Order> orders = orderDao.query(statementBuilder.prepare());

        System.out.println(orders.size());

        System.out.println(account1.getId() == orders.get(0).getAccount().getId());
        // 通过检查数据库中的每个字段，查看两个对象是否相等。
        System.out.println(orderDao.objectsEqual(order1, orders.get(0)));

        // Dear
        System.out.println(account1.getName());
        // Null
        System.out.println(orders.get(0).getAccount().getName());
        // refresh orders中的Order对象中的account 才能拿到其name
        accountDao.refresh(orders.get(0).getAccount());
        System.out.println();


    }
}
