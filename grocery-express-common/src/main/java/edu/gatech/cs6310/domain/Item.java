package edu.gatech.cs6310.domain;

import javax.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemName;
    private int weight;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Item(String itemName, int weight, Store store) {
        this.itemName = itemName;
        this.weight = weight;
        this.store = store;
    }

    public Item() {

    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return itemName + "," + weight;
    }
}
