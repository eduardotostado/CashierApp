package com.tostado.cashier.entities;

import java.math.BigDecimal;

public class Product extends QueryObject {

    private final int ID;
    private String name;
    private BigDecimal price;
    private int stock;
    private int storeID;
    private String storeName;

    public Product(int ID, String name, BigDecimal price, int stock, int storeID) {
        this.ID = ID;
        this.name = name;
        this.price = price.setScale(2);
        this.stock = stock;
        this.storeID = storeID;
    }

    public boolean edit(String name, BigDecimal price, int stock, int storeID){

        setName(name);
        setPrice(price);
        setStock(stock);
        setStoreID(storeID);

        statement = "UPDATE Product " +
                "SET " +
                "name = '" + this.getName() +  "', " +
                "price = " + this.getPrice() +  ", " +
                "stock = " + this.getStock() +  ", " +
                "storeID = " + this.getStoreID() +  " " +
                "WHERE ID = " + this.getID();

        return query(statement);
    }

    public boolean add(){
        statement = "INSERT INTO Product (name, price, stock, storeID) VALUES ('" +
                this.getName() + "', " + this.getPrice() + ", " + this.getStock() + ", " + this.getStoreID() +
                ")";

        return query(statement);
    }

    public boolean delete(){
        statement =
                "DELETE FROM Product WHERE ID = " +
                        this.getID();
        return query(statement);
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
