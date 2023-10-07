package edu.gatech.cs6310.customer.repository;

import edu.gatech.cs6310.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByAccount(String account);

}
