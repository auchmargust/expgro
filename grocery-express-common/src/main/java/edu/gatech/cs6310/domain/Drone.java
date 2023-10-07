package edu.gatech.cs6310.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIgnoreProperties("store")
public class Drone {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    protected Long id;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    // A drone’s identifier is not limited to integers – it could be a string.
    private String droneID;
    private int capacity;
    private int remainNumOfTrips;
    @OneToMany(mappedBy = "drone")
    @JsonIgnoreProperties("drone")
    private Map<String, Order> ordersAssigned = new TreeMap<>();
    @OneToOne
    @JoinColumn(name = "pilot_id", referencedColumnName = "uid")
    @JsonIgnoreProperties("drone")
    private DronePilot pilotAssigned;
    private int remainCap;
    private int fuelRate;
    private double maxFuelCapacity;
    private double remainFuel;
    private int currXCoordinate;
    private int currYCoordinate;

    public Drone(Store store, String droneID, int capacity, int remainNumOfTrips, int fuelRate, int maxFuelCapacity, int currXCoordinate, int currYCoordinate) {
        this.store = store;
        this.droneID = droneID;
        this.capacity = capacity;
        this.remainNumOfTrips = remainNumOfTrips;
        this.remainCap = capacity;
        // for fueling station
        this.fuelRate = fuelRate;
        this.maxFuelCapacity = maxFuelCapacity;
        this.remainFuel = maxFuelCapacity;
        this.currXCoordinate = currXCoordinate;
        this.currYCoordinate = currYCoordinate;
    }

    public Drone() {

    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public void setDroneID(String droneID) {
        this.droneID = droneID;
    }

    public String getDroneID() {
        return droneID;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setRemainNumOfTrips(int remainNumOfTrips) {
        this.remainNumOfTrips = remainNumOfTrips;
    }

    public void assignOrder(String orderID, Order order) {
        ordersAssigned.put(orderID, order);
    }

    public Map<String, Order> getOrdersAssigned() {
        return ordersAssigned;
    }

    public void assignPilot(DronePilot dronePilot) {
        pilotAssigned = dronePilot;
    }

    public DronePilot getPilotAssigned() {
        return pilotAssigned;
    }

    public void delPilot() {
        pilotAssigned = null;
    }

    public int getRemainNumOfTrips() {
        return remainNumOfTrips;
    }

    public void setRemainCap(int remainCap) {
        this.remainCap = remainCap;
    }

    public int getRemainCap() {
        return remainCap;
    }

    public int getFuelRate() {
        return fuelRate;
    }

    public void setFuelRate(int fuelRate) {
        this.fuelRate = fuelRate;
    }

    @Override
    public String toString() {
        return "droneID:" + droneID +
                ",total_cap:" + capacity +
                ",num_orders:" + ordersAssigned.size() +
                ",remaining_cap:" + getRemainCap() +
                ",trips_left:" + remainNumOfTrips +
                (pilotAssigned != null ? ",flown_by:" + pilotAssigned.getFirstName() + "_" + pilotAssigned.getLastName() : "") +
                ",fuel_rate:" + fuelRate +
                ",max_fuel_capacity:" + String.format("%.2f", maxFuelCapacity) +
                ",remaining_fuel:" + String.format("%.2f", remainFuel) +
                ",location:" + "(" + currXCoordinate + "," + currYCoordinate + ")";
    }

    //fueling station functions
    public double getDistance(int destinationX, int destinationY) {
        double distance = Math.sqrt((destinationY - currYCoordinate) * (destinationY - currYCoordinate) + (destinationX - currXCoordinate) * (destinationX - currXCoordinate));
        return distance;
    }
    public double getNeededFuel(int destinationX, int destinationY, double ordersWeight) {
        double distance = Math.sqrt((destinationY - currYCoordinate) * (destinationY - currYCoordinate) + (destinationX - currXCoordinate) * (destinationX - currXCoordinate));
        return fuelRate * distance * ordersWeight / capacity;
    }
    public double getMaxFuelCapacity() {
        return maxFuelCapacity;
    }

    public void setMaxFuelCapacity(double maxFuelCapacity) {
        this.maxFuelCapacity = maxFuelCapacity;
    }
    public double getRemainFuel() {
        return remainFuel;
    }

    public void setRemainFuel(double remainFuel) {
        this.remainFuel = remainFuel;
    }

    public void setCurrXCoordinate(int currXCoordinate) {
        this.currXCoordinate = currXCoordinate;
    }

    public void setCurrYCoordinate(int currYCoordinate) {
        this.currYCoordinate = currYCoordinate;
    }
    public int getCurrXCoordinate() {
        return currXCoordinate;
    }
    public int getCurrYCoordinate() {
        return currYCoordinate;
    }
}
