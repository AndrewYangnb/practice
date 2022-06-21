package com.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "orders")
public class Order {
    public static final String ACCOUNT_ID_FIELD_NAME = "account_id";

    @DatabaseField(generatedId = true)
    private int id;

    // 列名设置为"account_id"
    @DatabaseField(foreign = true, columnName = ACCOUNT_ID_FIELD_NAME)
    private Account_Foreign account;

    @DatabaseField
    private int itemNumber;

    @DatabaseField
    private int quantity;

    @DatabaseField
    private float price;

    Order() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public Order(Account_Foreign account, int itemNumber, float price, int quantity) {
        this.account = account;
        this.itemNumber = itemNumber;
        this.price = price;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public Account_Foreign getAccount() {
        return account;
    }

    public void setAccount(Account_Foreign account) {
        this.account = account;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
