package edu.gatech.cs6310.service.impl;

import edu.gatech.cs6310.domain.*;
import edu.gatech.cs6310.repository.*;
import edu.gatech.cs6310.service.CustomerService;
import edu.gatech.cs6310.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private FuelingStationRepository fuelingStationRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Store create(String name, int revenue, int storeXCoordinate, int storeYCoordinate) {
        Store existingStore = storeRepository.findByStoreName(name);
        if (existingStore != null) {
            throw new IllegalArgumentException("ERROR:store_identifier_already_exists");
        }

        Store newStore = new Store(name, revenue, storeXCoordinate, storeYCoordinate);

        String maxStationID = fuelingStationRepository.findMaxStationID();
        int newStationID = maxStationID == null ? 1 : Integer.parseInt(maxStationID) + 1;
        FuelingStation newFuelingStation = new FuelingStation(String.valueOf(newStationID), storeXCoordinate, storeYCoordinate, newStore);
        fuelingStationRepository.save(newFuelingStation);
        newStore.getFuelingStations().put(newFuelingStation.getStationID(), newFuelingStation);

        return storeRepository.save(newStore);
    }

    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Store findByName(String name) {
        return storeRepository.findByStoreName(name);
    }

    @Override
    public void addItemToStore(String storeName, String itemName, int weight) {
        Optional<Store> optionalStore = Optional.ofNullable(storeRepository.findByStoreName(storeName));
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            if (store.getItemByName(itemName) != null) {
                throw new RuntimeException("ERROR:item_identifier_already_exists");
            }
            Item newItem = new Item(itemName, weight, store);
            store.addItem(itemName, newItem);
            storeRepository.save(store);
        } else {
            throw new RuntimeException("ERROR:store_identifier_does_not_exist");
        }
    }

    @Override
    public List<Item> getAllItemsByStore(String storeName) {
        Optional<Store> optionalStore = Optional.ofNullable(storeRepository.findByStoreName(storeName));
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            List<Item> itemList = store.getItems().values().stream()
                    .sorted(Comparator.comparing(Item::getItemName))
                    .collect(Collectors.toList());
            return itemList;
        } else {
            throw new RuntimeException("ERROR:store_identifier_does_not_exist");
        }
    }

    @Override
    public void transferOrder(String storeName, String orderID, String droneID) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Order order = orderRepository.findByStoreAndOrderID(store, orderID);
        if (order == null) {
            throw new IllegalArgumentException("ERROR:order_identifier_does_not_exist");
        }

        Drone newDrone = droneRepository.findByStoreAndDroneID(store, droneID);
        if (newDrone == null) {
            throw new IllegalArgumentException("ERROR:drone_identifier_does_not_exist");
        }

        if (newDrone.getRemainCap() < order.getTotalWeight()) {
            throw new IllegalArgumentException("ERROR:new_drone_does_not_have_enough_capacity");
        }

        Drone oldDrone = order.getDrone();

        if (oldDrone == newDrone) {
            throw new IllegalArgumentException("OK: new_drone_is_current_drone_no_change");
        }

        oldDrone.getOrdersAssigned().remove(orderID);
        oldDrone.setRemainCap(oldDrone.getRemainCap() + order.getTotalWeight());
        newDrone.assignOrder(orderID, order);
        order.setDrone(newDrone);
        newDrone.setRemainCap(newDrone.getRemainCap() - order.getTotalWeight());

        // update efficiency
        store.setTransfers(store.getTransfers() + 1);
    }

    @Override
    public void makeFuelingStation(String stationID, int xCoordinate, int yCoordinate, String storeName) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        if (fuelingStationRepository.findByStationID(stationID) != null) {
            throw new IllegalArgumentException("ERROR:station_identifier_already_exists");
        }

        FuelingStation fuelingStation = new FuelingStation(stationID, xCoordinate, yCoordinate, store);
        fuelingStationRepository.save(fuelingStation);
        store.getFuelingStations().put(stationID, fuelingStation);
    }

    @Override
    public void deleteFuelingStation(String stationID) {
        FuelingStation fuelingStation = fuelingStationRepository.findByStationID(stationID);
        if (fuelingStation == null) {
            throw new IllegalArgumentException("ERROR:station_identifier_does_not_exist");
        }

        fuelingStationRepository.deleteByStationID(stationID);
    }

    @Override
    public void assignCoupon(String storeName, String account, String couponId, int couponReduction, int expiryPeriod) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Customer customer = customerRepository.findByAccount(account);
        if (customer == null) {
            throw new IllegalArgumentException("ERROR:customer_identifier_does_not_exist");
        }

        if (couponRepository.findByCouponId(couponId) != null) {
            throw new IllegalArgumentException("ERROR:coupon_identifier_already_exists");
        }
        Random random = new Random();
        int totalRatings = 0;
        for (Customer c : customerRepository.findAll()) {
            totalRatings += c.getRating();
        }
        int randomValue = random.nextInt(totalRatings) + 1;
        int cumulativeRatings = 0;
        String selectedAccount = null;
        for (Customer c : customerRepository.findAll()) {
            cumulativeRatings += c.getRating();
            if (randomValue <= cumulativeRatings) {
                selectedAccount = c.getAccount();
            }
        }

        //String account = randomCustomerSelection();
        Coupon newCoupon = new Coupon(store, couponId, couponReduction, expiryPeriod);
        store.getCoupons().put(couponId, newCoupon);
        customer.assignCoupons(newCoupon);
        newCoupon.setCustomer(customer);
        couponRepository.save(newCoupon);




    }

    @Override
    public List<Coupon> displayCoupons(String storeName) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        List<Coupon> couponList = new ArrayList<>();
        for (Coupon coupon : store.getCoupons().values()) {
            couponList.add(coupon);
        }
        return couponList;
    }


}