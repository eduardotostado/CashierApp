package com.tostado.cashier.entities;

import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;

public class Transaction extends QueryObject {

    private final int ID;
    private int productID;
    private String productName;
    private BigDecimal productCost;
    private int quantity;
    private BigDecimal total;
    private int employeeID;
    private String employeeName;
    private int storeID;
    private String storeName;
    private int customerID;
    private String customerName;
    private String date;

    public Transaction(int ID, int productID, int quantity, BigDecimal total, BigDecimal productCost, int employeeID, int storeID, int customerID, String date) {
        this.ID = ID;
        this.productID = productID;
        this.quantity = quantity;
        this.productCost = productCost;
        if(total.equals(new BigDecimal("0.00").setScale(2))) {
            calculateTotal();
        }else{this.total = total;}
        this.employeeID = employeeID;
        this.storeID = storeID;
        this.customerID = customerID;
        this.date = date;
    }

    public boolean add(){
        statement = "INSERT INTO Transaction (productID, quantity, total, employeeID, storeID, customerID, date) VALUES (" +
                this.getProductID() + ", " + this.getQuantity() + ", " +
                this.getTotal() + ", " + this.getEmployeeID() + ", " +
                this.getStoreID() +  ", " + this.getCustomerID() +  ", '" +
                this.getDate() +
                "')";

        return query(statement);
    }

    public boolean delete(){
        statement =
                "DELETE FROM Transaction WHERE ID = " +
                        this.getID();
        return query(statement);
    }

    public int getID() {
        return ID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setProductCost(BigDecimal productCost) {
        this.productCost = productCost;
    }

    public BigDecimal getProductCost(){
        return this.productCost;
    }

    public void calculateTotal() {
        System.out.println(getQuantity());
        System.out.println(getProductCost());
        if(this.productCost != null) {
            this.total = BigDecimal.valueOf(getQuantity()).multiply(getProductCost());
        }
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
