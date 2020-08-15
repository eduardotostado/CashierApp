package com.tostado.cashier.entities;

public class Store extends QueryObject {

    private final int ID;
    private String name;
    private String address;
    private String phoneNumber;

    public Store(int ID, String name, String address, String phoneNumber) {
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
    }

    public Store(int ID, String name){
        this.ID = ID;
        this.name = name;
    }

    public boolean edit(String name, String address, String phoneNumber){

        setName(name);
        setAddress(address);
        setPhoneNumber(phoneNumber);

        statement = "UPDATE Store " +
                "SET " +
                "name = '" + this.getName() +  "', " +
                "address = '" + this.getAddress() +  "', " +
                "phoneNumber = '" + this.getPhoneNumber().replaceAll("[\\s\\-()]", "") +  "' " +
                "WHERE ID = " + this.getID();

        return query(statement);
    }

    public boolean add(){
        statement = "INSERT INTO Store (name, address, phoneNumber) VALUES ('" +
                this.getName() + "', '" + this.getAddress() + "', '" + this.getPhoneNumber().replaceAll("[\\s\\-()]", "") + "'" +
                ")";

        return query(statement);
    }

    public boolean delete(){
        statement =
                "DELETE FROM Store WHERE ID = " +
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

    public String toString(){
        return this.getName();
    }
}
