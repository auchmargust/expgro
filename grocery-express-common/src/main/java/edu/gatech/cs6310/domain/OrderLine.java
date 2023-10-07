package edu.gatech.cs6310.domain;

import javax.persistence.*;

@Entity
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;
    private int quantity;
    private int unitPrice;
    // derived attribute
    private int subTotal;
    private int subWeight;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    public OrderLine(Item item, int quantity, int unitPrice, Order order) {
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.order = order;
        this.subTotal = quantity * unitPrice;
        this.subWeight = quantity * item.getWeight();
    }

    public OrderLine() {

    }

    public int getSubTotal() {
        return subTotal;
    }

    public int getSubWeight() {
        return subWeight;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return "item_name:" + item.getItemName() + ",total_quantity:" + quantity + ",total_cost:" + getSubTotal() + ",total_weight:" + getSubWeight();
    }

}
