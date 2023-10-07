package edu.gatech.cs6310.service;

import edu.gatech.cs6310.domain.Order;

import java.util.List;

public interface OrderService {
    Order startOrder(String storeName, String orderID, String droneID, String account);

    List<Order> getOrdersByStore(String storeName);

    void requestItem(String storeName, String orderId, String itemName, int quantity, int unitPrice);

    void purchaseOrder(String storeName, String orderID);

    void cancelOrder(String storeName, String orderID);
}
