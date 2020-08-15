package com.tostado.cashier.entities;

public class Employee extends QueryObject {

    private final int ID;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private int storeID;
    private boolean managerFlag;
    private String storeName;

    public Employee(int ID, String firstName, String lastName, String email, String address, String phoneNumber, int storeID, boolean managerFlag) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");;
        this.storeID = storeID;
        this.managerFlag = managerFlag;
    }

    public Employee(int ID, String firstName, String lastName, String email){
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public boolean edit(String firstName, String lastName, String email, String address, String phoneNumber, int storeID, boolean managerFlag){

        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setAddress(address);
        setPhoneNumber(phoneNumber);
        setStoreID(storeID);
        setManagerFlag(managerFlag);

        statement = "UPDATE Employee " +
                "SET " +
                "firstName = '" + this.getFirstName() +  "', " +
                "lastName = '" + this.getLastName() +  "', " +
                "email = '" + this.getEmail() +  "', " +
                "address = '" + this.getAddress() +  "', " +
                "phoneNumber = '" + this.getPhoneNumber().replaceAll("[\\s\\-()]", "")  +  "', " +
                "storeID = " + this.getStoreID() + ", " +
                "isManager = " + this.getManagerFlag() +  " " +
                "WHERE ID = " + this.getID();

        return query(statement);
    }

    public boolean add(){
        statement = "INSERT INTO Employee (firstName, lastName, email, address, phoneNumber, storeID, isManager) VALUES ('" +
                this.getFirstName() + "', '" + this.getLastName() +  "', '" + this.getEmail() + "', '" + this.getAddress() + "', '" + this.getPhoneNumber().replaceAll("[\\s\\-()]", "") + "', " + this.getStoreID() + ", " + this.getManagerFlag() +
                ")";

        return query(statement);
    }

    public boolean delete(){
        statement =
                "DELETE FROM Employee WHERE ID = " +
                        this.getID();
        return query(statement);
    }

    public int getID() {
        return ID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public boolean getManagerFlag() {
        return this.managerFlag;
    }

    public void setManagerFlag(boolean managerFlag) {
        this.managerFlag = managerFlag;
    }

    public String getStoreName(){
        return this.storeName;
    }

    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    public String toString(){
        return this.getFirstName() + " " + this.getLastName();
    }
}
