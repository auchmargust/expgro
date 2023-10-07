package edu.gatech.cs6310.service;

import edu.gatech.cs6310.domain.Customer;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(String firstName, String lastName, String phone, String account, int rating, int credit, int xCoordinate, int yCoordinate);

    List<Customer> getAllCustomers();

    Customer findByAccount(String account);



}
