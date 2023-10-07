package edu.gatech.cs6310.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
public class FuelingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stationID;

    private int xCoordinate;
    private int yCoordinate;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnoreProperties("fuelingStations")
    private Store store;
    public FuelingStation(String stationID, int xCoordinate, int yCoordinate, Store store) {
        this.stationID = stationID;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.store = store;
    }

    public FuelingStation() {

    }

    public String getStationID() {
        return stationID;
    }
    public int getxCoordinate() {
        return xCoordinate;
    }
    public int getyCoordinate() {
        return yCoordinate;
    }

    public Store getStore() {
        return store;
    }

    @Override
    public String toString() {
        return "stationID:" + getStationID() +
                ",location:" + "(" + getxCoordinate() + "," + getyCoordinate() + ")";
    }
}