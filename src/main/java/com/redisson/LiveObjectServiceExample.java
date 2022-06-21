package com.redisson;

import com.util.RedissonAdapter;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;
import org.redisson.liveobject.resolver.LongGenerator;
import org.redisson.liveobject.resolver.UUIDGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Live Object 分布式实时对象
 * 不仅可以被一个JVM里的各个线程相引用, 还可以被多个位于不同JVM里的线程同时引用
 */

public class LiveObjectServiceExample {

    // 创建一个类
    // 注释@Entity 用来特指此类为一个实时对象类
    @REntity
    public static class Product implements Serializable {

        @RId  // 特指该字段为Live Object的字段, 每个类只能指定一个该字段
        private Long id;

        private String name;

        private Map<String, Integer> itemName2Amount;

        private BigDecimal price;

        private Integer unitInStock;

        protected Product() {
        }

        public Product(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setUnitsInStock(Integer unitInStock) {
            this.unitInStock = unitInStock;
        }

        public Integer getUnitInStock() {
            return unitInStock;
        }

        public String getName() {
            return  name;
        }

        public Map<String, Integer> getItemName2Amount() {
            return itemName2Amount;
        }

    }

    @REntity
    public static class OrderDetail implements Serializable {

        @RId(generator = LongGenerator.class)
        private Long id;

        private Order order;

        private Product product;

        private BigDecimal price;

        private Integer quantity;

        private BigDecimal discount;

        protected OrderDetail() {

        }

        public OrderDetail(Order order, Product product) {
            super();
            this.order = order;
            this.product = product;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public Long getId() {
            return id;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public Product getProduct() {
            return  product;
        }

    }

    @REntity

    public static class Customer implements Serializable {

        @RId(generator = UUIDGenerator.class)
        private String id;

        private List<Order> orders;

        private String name;

        private String address;

        private String phone;

        protected Customer() {
        }

        public Customer(String id) {
            super();
            this.id = id;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }

        public void addOrder(Order order) {
            orders.add(order);
        }

        public List<Order> getOrders() {
            return orders;
        }

        public String getId() {
            return id;
        }
    }

    @REntity
    public static class Order implements Serializable {

        @RId(generator = LongGenerator.class)
        private Long id;

        private List<OrderDetail> orderDetails;

        private Customer customer;

        private Date date;

        private Date shippedDate;

        private String shipName;

        private String shipAddress;

        private String shipPostalCode;

        protected Order() {
        }

        public Order(Customer customer) {
            super();
            this.customer = customer;
        }

        public List<OrderDetail> getOrderDetails() {
            return orderDetails;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Long getId() {
            return id;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Date getShippedDate() {
            return shippedDate;
        }

        public void setShippedDate(Date shippedDate) {
            this.shippedDate = shippedDate;
        }

        public String getShipName() {
            return shipName;
        }

        public void setShipName(String shipName) {
            this.shipName = shipName;
        }

        public String getShipAddress() {
            return shipAddress;
        }

        public void setShipAddress(String shipAddress) {
            this.shipAddress = shipAddress;
        }

        public String getShipPostalCode() {
            return shipPostalCode;
        }

        public void setShipPostalCode(String shipPostalCode) {
            this.shipPostalCode = shipPostalCode;
        }

    }

    public static void main(String[] args) {

        // 创建一个实时对象服务
        RLiveObjectService liveObjectService = RedissonAdapter.get("server").getLiveObjectService();

        // 实例化一个Customer对象
        Customer customer = new Customer("12");
        // 将custom对象转换为实时对象
        customer = liveObjectService.merge(customer);

        customer.setName("Alexander Pushkin");
        customer.setPhone("+7193127489123");
        customer.setAddress("Moscow, Tverskaya str");

        // 实例化Product对象
        Product product = new Product(1L, "FoodBox");
        // product object is becoming "live" object
        product = liveObjectService.merge(product);

        product.getItemName2Amount().put("apple", 1);
        product.getItemName2Amount().put("banana", 12);
        product.setPrice(BigDecimal.valueOf(10));
        product.setUnitsInStock(12);

        Order order = new Order(customer);
        // order object is becoming "live" object
        order = liveObjectService.merge(order);

        order.setDate(new Date());
        order.setShipAddress("Moscow, Gasheka str");
        order.setShipName("James Bond");
        order.setShipPostalCode("141920");

        OrderDetail od = new OrderDetail(order, product);
        // OrderDetail object is becoming "live" object
        od = liveObjectService.merge(od);
        od.setPrice(BigDecimal.valueOf(9));
        od.setQuantity(1);
        order.getOrderDetails().add(od);
        customer.getOrders().add(order);

        RedissonAdapter.get("server").shutdown();
    }

}
