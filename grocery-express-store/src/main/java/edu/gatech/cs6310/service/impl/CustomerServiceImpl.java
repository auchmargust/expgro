package edu.gatech.cs6310.service.impl;

import edu.gatech.cs6310.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.gatech.cs6310.domain.Customer;
import edu.gatech.cs6310.repository.CustomerRepository;
import edu.gatech.cs6310.service.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(String firstName, String lastName, String phone, String account, int rating, int credit, int xCoordinate, int yCoordinate) {
        Customer existingCustomer = customerRepository.findByAccount(account);
        if (existingCustomer != null) {
            throw new IllegalArgumentException("ERROR:customer_identifier_already_exists");
        }
        Customer customer = new Customer(firstName, lastName, phone, account, rating, credit, xCoordinate, yCoordinate);
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findByAccount(String account) {
        return customerRepository.findByAccount(account);
    }

    public String randomCustomerSelection(){
        Map<String, Integer> entries = new TreeMap();
        int accumulatedWeight = 0;
        double r = Math.random();
        String selectedAccountId=null;

        for (Customer c: customerRepository.findAll()){
            accumulatedWeight += c.getCredit();
            entries.put(c.getAccount(),accumulatedWeight);
        }

        for (Map.Entry<String,Integer>entry : entries.entrySet()){
            if (entry.getValue()>=r){
                selectedAccountId=entry.getKey();
            }
        }
        return selectedAccountId;

    }

}
