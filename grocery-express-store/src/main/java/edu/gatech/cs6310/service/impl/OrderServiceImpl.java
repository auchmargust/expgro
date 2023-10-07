package edu.gatech.cs6310.service.impl;

import edu.gatech.cs6310.domain.*;
import edu.gatech.cs6310.repository.*;
import edu.gatech.cs6310.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DronePilotRepository dronePilotRepository;

    @Override
    public Order startOrder(String storeName, String orderID, String droneID, String account) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Order existingOrder = orderRepository.findByStoreAndOrderID(store, orderID);
        if (existingOrder != null) {
            throw new IllegalArgumentException("ERROR:order_identifier_already_exists");
        }

        Drone drone = droneRepository.findByStoreAndDroneID(store, droneID);
        if (drone == null) {
            throw new IllegalArgumentException("ERROR:drone_identifier_does_not_exist");
        }

        Customer customer = customerRepository.findByAccount(account);
        if (customer == null) {
            throw new IllegalArgumentException("ERROR:customer_identifier_does_not_exist");
        }

        Order newOrder = new Order(orderID, customer, drone, store);
        store.addOrder(orderID, newOrder);
        drone.assignOrder(orderID, newOrder);

        return orderRepository.save(newOrder);
    }

    @Override
    public List<Order> getOrdersByStore(String storeName) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        return orderRepository.findByStore(store);
    }

    @Override
    public void requestItem(String storeName, String orderID, String itemName, int quantity, int unitPrice) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Order order = orderRepository.findByStoreAndOrderID(store, orderID);
        if (order == null) {
            throw new IllegalArgumentException("ERROR:order_identifier_does_not_exist");
        }

        Item item = store.getItems().get(itemName);
        if (item == null) {
            throw new IllegalArgumentException("ERROR:item_identifier_does_not_exist");
        }

        if (order.getOrderLines().containsKey(itemName)) {
            throw new IllegalArgumentException("ERROR:item_already_ordered");
        }

        Customer customer = order.getCustomer();
        if (customer.getCredit() < (customer.getTempCost() + quantity * unitPrice)) {
            throw new IllegalArgumentException("ERROR:customer_cant_afford_new_item");
        }

        if (order.getDrone().getRemainCap() < quantity * item.getWeight()) {
            throw new IllegalArgumentException("ERROR:drone_cant_carry_new_item");
        }

        OrderLine orderLine = new OrderLine(item, quantity, unitPrice, order);
        order.addOrderLine(orderLine);
        orderRepository.save(order);
    }

    @Override
    public void purchaseOrder(String storeName, String orderID) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Order order = orderRepository.findByStoreAndOrderID(store, orderID);
        if (order == null) {
            throw new IllegalArgumentException("ERROR:order_identifier_does_not_exist");
        }

        Drone drone = order.getDrone();
        if (drone.getRemainNumOfTrips() <= 0) {
            throw new IllegalArgumentException("ERROR:drone_needs_fuel");
        }

        DronePilot pilot = drone.getPilotAssigned();
        if (pilot == null) {
            throw new IllegalArgumentException("ERROR:drone_needs_pilot");
        }

        // deduct the cost from the customer's credit
        // use coupon if available
        Customer customer = order.getCustomer();
        int totalCost = order.getTotalCost();
        if (customer.getCoupons() != null && customer.getCoupons().size() > 0) {
            Coupon coupon = customer.getCoupons().get(0);

            int couponReduction = coupon.getCouponReduction();

            // update the customer's credit
            int costAfterCoupon = totalCost - couponReduction;
            customer.setCredit(customer.getCredit() - costAfterCoupon);
            customer.setTempCost(customer.getTempCost() - costAfterCoupon);

            // add the cost to the store's revenue
            store.setInitialRev(store.getInitialRev() + costAfterCoupon);

            // penalty if coupon is expired
            if (coupon.isExpiry()) {
                store.setInitialRev(store.getInitialRev() - 10);
            }

        } else {
            customer.setCredit(customer.getCredit() - totalCost);
            customer.setTempCost(customer.getTempCost() - totalCost);
            // add the cost to the store's revenue
            store.setInitialRev(store.getInitialRev() + totalCost);
        }

        // reduce the remaining number of trips for the drone
        drone.setRemainNumOfTrips(drone.getRemainNumOfTrips() - 1);

        // increase the number of successful deliveries for the pilot
        pilot.setNumOfSuccDel(pilot.getNumOfSuccDel() + 1);

        // update the remaining capacity of the drone
        drone.setRemainCap(drone.getRemainCap() + order.getTotalWeight());
        // update remaining fuel and location of the drone
        int destinationX = order.getCustomer().getxCoordinate();
        int destinationY = order.getCustomer().getyCoordinate();
        drone.setRemainFuel(drone.getRemainFuel() - drone.getNeededFuel(destinationX, destinationY, order.getTotalWeight()));
        drone.setCurrXCoordinate(destinationX);
        drone.setCurrYCoordinate(destinationY);

        // remove the order
        store.getOwnedOrders().remove(orderID);
        drone.getOrdersAssigned().remove(orderID);
        orderRepository.deleteByStoreAndOrderID(store, orderID);

        // update efficiency
        store.setPurchases(store.getPurchases() + 1);
        store.setOverloads(store.getOverloads() + drone.getOrdersAssigned().size());

        storeRepository.save(store);
        customerRepository.save(customer);
        droneRepository.save(drone);
        dronePilotRepository.save(pilot);

    }

    @Override
    public void cancelOrder(String storeName, String orderID) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Order order = orderRepository.findByStoreAndOrderID(store, orderID);
        if (order == null) {
            throw new IllegalArgumentException("ERROR:order_identifier_does_not_exist");
        }

        Drone drone = order.getDrone();

        store.getOwnedOrders().remove(orderID);
        drone.getOrdersAssigned().remove(orderID);
        drone.setRemainCap(drone.getRemainCap() + order.getTotalWeight());
        orderRepository.deleteByStoreAndOrderID(store, orderID);
    }

}
