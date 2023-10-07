package edu.gatech.cs6310.repository;

import edu.gatech.cs6310.domain.Order;
import edu.gatech.cs6310.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderID(String orderID);
    Order findByStoreAndOrderID(Store store, String orderID);

    List<Order> findByStore(Store store);

    void deleteByStoreAndOrderID(Store store, String orderID);
}
