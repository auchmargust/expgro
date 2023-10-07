package edu.gatech.cs6310.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderID;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "drone_id")
    @JsonIgnoreProperties("ordersAssigned")
    private Drone drone;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Map<String, OrderLine> orderLines = new TreeMap<>();
    private int totalCost;
    private int totalWeight;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Order(String orderID, Customer customer, Drone drone, Store store) {
        this.orderID = orderID;
        this.customer = customer;
        this.drone = drone;
        this.store = store;
    }

    public Order() {

    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Drone getDrone() {
        return drone;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public Map<String, OrderLine> getOrderLines() {
        return orderLines;
    }

    public void addOrderLine(OrderLine orderLine) {
        // check "ERROR:item_already_ordered"
        if (orderLines.containsKey(orderLine.getItem().getItemName())) {
            System.out.println("ERROR:item_already_ordered");
        } else if (customer.getCredit() < (customer.getTempCost() + orderLine.getSubTotal())) {
            // check "ERROR:customer_cant_afford_new_item"
            System.out.println("ERROR:customer_cant_afford_new_item");
        } else if (drone.getRemainCap() < orderLine.getSubWeight()) {
            // check "ERROR:drone_cant_carry_new_item"
            System.out.println("ERROR:drone_cant_carry_new_item");
        } else {
            orderLines.put(orderLine.getItem().getItemName(), orderLine);
            totalCost += orderLine.getSubTotal();
            totalWeight += orderLine.getSubWeight();
            customer.setTempCost(customer.getTempCost() + orderLine.getSubTotal());
            drone.setRemainCap(drone.getRemainCap() - orderLine.getSubWeight());
            System.out.println("OK:change_completed");
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("orderID:" + orderID);

        orderLines.values().forEach(orderLine -> {
            str.append("\n");
            str.append(orderLine.toString());
        });

        return str.toString();
    }
}
