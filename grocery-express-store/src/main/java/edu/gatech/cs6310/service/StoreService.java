package edu.gatech.cs6310.service;

import edu.gatech.cs6310.domain.Coupon;
import edu.gatech.cs6310.domain.Item;
import edu.gatech.cs6310.domain.Store;

import java.util.List;

public interface StoreService {

    Store create(String name, int revenue, int storeXCoordinate, int storeYCoordinate);

    List<Store> getAllStores();

    Store findByName(String name);

    void addItemToStore(String storeName, String itemName, int weight);

    List<Item> getAllItemsByStore(String storeName);

    void transferOrder(String storeName, String orderID, String droneID);

    void makeFuelingStation(String stationID, int xCoordinate, int yCoordinate, String storeName);

    void deleteFuelingStation(String stationID);

    void assignCoupon(String storeName, String account, String couponId, int couponReduction, int expiryPeriod);

    List<Coupon> displayCoupons(String storeName);

}
