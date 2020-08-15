package com.tostado.cashier.entities;

public class Customer extends QueryObject {

    private final int ID;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String address;

    public Customer(int ID, String phoneNumber, String firstName, String lastName, String address) {
        this.ID = ID;
        this.phoneNumber = phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public boolean edit(String phoneNumber, String firstName, String lastName, String address){

        setPhoneNumber(phoneNumber);
        setFirstName(firstName);
        setLastName(lastName);
        setAddress(address);

        statement = "UPDATE Customer " +
                "SET " +
                "phoneNumber = '" + this.getPhoneNumber().replaceAll("[\\s\\-()]", "") +  "', " +
                "firstName = '" + this.getFirstName() +  "', " +
                "lastName = '" + this.getLastName() +  "', " +
                "address = '" + this.getAddress() +  "' " +
                "WHERE ID = " + this.getID();

        return query(statement);
    }

    public boolean add(){
        statement = "INSERT INTO Customer (phoneNumber, firstName, lastName, address) VALUES ('" +
                this.getPhoneNumber().replaceAll("[\\s\\-()]", "") + "', '" + this.getFirstName() + "', '" + this.getLastName() + "', '" + this.getAddress() + "' " +
                ")";

        return query(statement);
    }

    public boolean delete(){
        statement =
                "DELETE FROM Customer WHERE ID = " +
                        this.getID();
        return query(statement);
    }

    public int getID() {
        return ID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
