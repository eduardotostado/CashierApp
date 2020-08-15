package com.tostado.cashier.entities;

import java.security.NoSuchAlgorithmException;

public class User extends QueryObject {

    private String username;
    private String password;
    private int employeeID;
    private String employeeName;
    private String employeeEmail;

    public User(String username, String password, int employeeID) {
        this.username = username;
        this.employeeID = employeeID;
        this.password = password;
    }

    public User(String username, int employeeID){
        this.username = username;
        this.employeeID = employeeID;
    }

    public boolean edit(String password, int employeeID) throws NoSuchAlgorithmException {

        setPassword(password);
        setEmployeeID(employeeID);

        statement = "UPDATE User " +
                "SET " +
                "password = '" + this.getPassword() +  "', " +
                "employeeID = " + this.getEmployeeID() +  " " +
                "WHERE username = '" + this.getUsername() + "'";

        return query(statement);
    }

    public boolean add(){
        statement = "INSERT INTO User (username, password, employeeID) VALUES ('" +
                this.getUsername() + "', '" + this.getPassword() + "', "  + this.getEmployeeID() +
                ")";
        return query(statement);
    }

    public boolean delete(){
        statement =
                "DELETE FROM User WHERE username = '" +
                        this.getUsername() + "'";
        return query(statement);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
}
