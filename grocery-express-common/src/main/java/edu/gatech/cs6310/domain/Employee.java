package edu.gatech.cs6310.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Employee extends User {

    protected String taxID;
    protected int numOfMonth;
    protected int salary;

    public Employee(String firstName, String lastName, String phone, String taxID, int numOfMonth, int salary) {
        super(firstName, lastName, phone);
        this.taxID = taxID;
        this.numOfMonth = numOfMonth;
        this.salary = salary;
    }

    public Employee() {

    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setNumOfMonth(int numOfMonth) {
        this.numOfMonth = numOfMonth;
    }

    public int getNumOfMonth() {
        return numOfMonth;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getSalary() {
        return salary;
    }
}
