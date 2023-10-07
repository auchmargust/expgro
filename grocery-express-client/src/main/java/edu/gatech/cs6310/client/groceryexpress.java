package edu.gatech.cs6310.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import edu.gatech.cs6310.domain.*;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Scanner;

public class groceryexpress {

    private static String baseUrl = "http://localhost:8080";

    private static Scanner scanner = new Scanner(System.in);

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(Level.ERROR);
        });
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            baseUrl = args[0];
        }
        String title = "Grocery Express\n";
        System.out.println("\033[1;33m" + title + "\033[0m");

        System.out.println("Welcome to the Grocery Express System!");
        System.out.println("---------------------------------------");
        System.out.println("Please enter commands:");

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("stop")) {
                System.out.println("stop acknowledged\n" + "Goodbye!");
                break;
            } else {
                handleCommand(command);
            }
        }
    }

    private static void handleCommand(String command) {
        String[] tokens = command.split(",");
        String action = tokens[0];

        if (action.startsWith("//")) {
            System.out.println("Please enter a command. Type 'help' for a list of commands.");
            return;
        }

        switch (action) {
            case "make_store":
                makeStore(command);
                System.out.println();
                break;
            case "display_stores":
                displayStores();
                System.out.println();
                break;
            case "sell_item":
                sellItem(command);
                System.out.println();
                break;
            case "display_items":
                displayItems(command);
                System.out.println();
                break;
            case "make_pilot":
                makePilot(command);
                System.out.println();
                break;
            case "display_pilots":
                displayPilots();
                System.out.println();
                break;
            case "make_drone":
                makeDrone(command);
                System.out.println();
                break;
            case "display_drones":
                displayDrones(command);
                System.out.println();
                break;
            case "fly_drone":
                flyDrone(command);
                System.out.println();
                break;
            case "make_customer":
                makeCustomer(command);
                System.out.println();
                break;
            case "display_customers":
                displayCustomers();
                System.out.println();
                break;
            case "start_order":
                startOrder(command);
                System.out.println();
                break;
            case "display_orders":
                displayOrders(command);
                System.out.println();
                break;
            case "request_item":
                requestItem(command);
                System.out.println();
                break;
            case "purchase_order":
                purchaseOrder(command);
                System.out.println();
                break;
            case "cancel_order":
                cancelOrder(command);
                System.out.println();
                break;
            case "transfer_order":
                transferOrder(command);
                System.out.println();
                break;
            case "display_efficiency":
                displayEfficiency();
                System.out.println();
                break;
            case "make_fueling_station":
                makeFuelingStation(command);
                System.out.println();
                break;
            case "delete_fueling_station":
                deleteFuelingStation(command);
                System.out.println();
                break;
            case "check_refueling_option":
                checkRefuelingOption(command);
                System.out.println();
                break;
            case "refuel_drone":
                refuelDrone(command);
                System.out.println();
                break;
            case "assign_coupon":
                assignCoupon(command);
                System.out.println();
                break;
            case "display_coupon":
                displayCoupon(command);
                System.out.println();
                break;
            case "help":
                showHelp();
                System.out.println();
                break;
            default:
                System.out.println("Invalid command. Type 'help' for a list of commands.");
        }
    }

    private static void makeStore(String command) {
        String[] tokens = command.split(",");
        try {
            String name = tokens[1];
            int revenue = Integer.parseInt(tokens[2]);
            int storeXCoordinate = Integer.parseInt(tokens[3]);
            int storeYCoordinate = Integer.parseInt(tokens[4]);
            String url = baseUrl + "/store?name=" + name + "&revenue=" + revenue + "&storeXCoordinate=" + storeXCoordinate + "&storeYCoordinate=" + storeYCoordinate;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    private static void displayStores() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String storesUrl = baseUrl + "/stores";
            ResponseEntity<List<Store>> response = restTemplate.exchange(storesUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Store>>() {
            });
            List<Store> storeList = response.getBody();
            for (Store store : storeList) {
                System.out.println(store.toString());
            }
            System.out.println("OK:display_completed");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    private static void sellItem(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String itemName = tokens[2];
            int weight = Integer.parseInt(tokens[3]);
            String url = baseUrl + "/store/sellItem?storeName=" + storeName + "&itemName=" + itemName + "&weight=" + weight;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    private static void displayItems(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String url = baseUrl + "/store/items?storeName=" + storeName;
            RestTemplate restTemplate = new RestTemplate();
            ParameterizedTypeReference<List<Item>> responseType = new ParameterizedTypeReference<List<Item>>() {
            };
            ResponseEntity<List<Item>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            List<Item> items = responseEntity.getBody();
            for (Item item : items) {
                System.out.println(item.toString());
            }
            System.out.println("OK:display_completed");
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR:store_identifier_does_not_exist");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void makePilot(String command) {
        String[] tokens = command.split(",");
        try {
            String account = tokens[1];
            String firstName = tokens[2];
            String lastName = tokens[3];
            String phone = tokens[4];
            String taxID = tokens[5];
            String licenseId = tokens[6];
            int numOfSuccDel = Integer.parseInt(tokens[7]);
            String url = baseUrl + "/dronepilot?account=" + account + "&firstName=" + firstName + "&lastName=" + lastName
                    + "&phone=" + phone + "&taxID=" + taxID + "&licenseId=" + licenseId + "&numOfSuccDel=" + numOfSuccDel;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void displayPilots() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = baseUrl + "/dronepilots";
            ResponseEntity<List<DronePilot>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<DronePilot>>() {
            });
            List<DronePilot> pilotList = response.getBody();
            for (DronePilot pilot : pilotList) {
                System.out.println(pilot.toString());
            }
            System.out.println("OK:display_completed");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void makeDrone(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String droneID = tokens[2];
            int capacity = Integer.parseInt(tokens[3]);
            int remainNumOfTrips = Integer.parseInt(tokens[4]);
            int fuelRate = Integer.parseInt(tokens[5]);
            int maxFuelCapacity = Integer.parseInt(tokens[6]);
            int currXCoordinate = Integer.parseInt(tokens[7]);
            int currYCoordinate = Integer.parseInt(tokens[8]);
            String url = baseUrl + "/drone/" + storeName + "?droneID=" + droneID + "&capacity=" + capacity + "&remainNumOfTrips=" + remainNumOfTrips + "&fuelRate=" + fuelRate + "&maxFuelCapacity=" + maxFuelCapacity + "&currXCoordinate=" + currXCoordinate + "&currYCoordinate=" + currYCoordinate;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    private static void displayDrones(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String url = baseUrl + "/stores/" + storeName + "/drones";
            RestTemplate restTemplate = new RestTemplate();
            ParameterizedTypeReference<List<Drone>> responseType = new ParameterizedTypeReference<List<Drone>>() {
            };
            ResponseEntity<List<Drone>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            List<Drone> drones = responseEntity.getBody();
            for (Drone drone : drones) {
                System.out.println(drone.toString());
            }
            System.out.println("OK:display_completed");
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR:store_identifier_does_not_exist");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    private static void flyDrone(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String droneID = tokens[2];
            String account = tokens[3];
            String url = baseUrl + "/drone/fly?storeName=" + storeName + "&droneID=" + droneID + "&account=" + account;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void makeCustomer(String command) {
        String[] tokens = command.split(",");
        if (tokens[0].equals("make_customer")) {
            try {
                String account = tokens[1];
                String firstName = tokens[2];
                String lastName = tokens[3];
                String phone = tokens[4];
                int rating = Integer.parseInt(tokens[5]);
                int credit = Integer.parseInt(tokens[6]);
                int xCoordinate = Integer.parseInt(tokens[7]);
                int yCoordinate = Integer.parseInt(tokens[8]);
                String url = baseUrl + "/customer?firstName=" + firstName + "&lastName=" + lastName + "&phone=" + phone + "&account=" + account + "&rating=" + rating + "&credit=" + credit+ "&xCoordinate=" + xCoordinate+ "&yCoordinate=" + yCoordinate;
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
                System.out.println(response.getBody());
            } catch (HttpClientErrorException e) {
                System.out.println(e.getResponseBodyAsString());
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void displayCustomers() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = baseUrl + "/customers";
            ResponseEntity<List<Customer>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>>() {
            });
            List<Customer> customerList = response.getBody();
            for (Customer customer : customerList) {
                System.out.println(customer.toString());
            }
            System.out.println("OK:display_completed");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void startOrder(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String orderID = tokens[2];
            String droneID = tokens[3];
            String account = tokens[4];
            String url = baseUrl + "/start_order?storeName=" + storeName + "&orderID=" + orderID + "&droneID=" + droneID + "&account=" + account;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println("OK:change_completed");
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void displayOrders(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String url = baseUrl + "/store/" + storeName + "/orders";
            RestTemplate restTemplate = new RestTemplate();
            ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<List<Order>>() {
            };
            ResponseEntity<List<Order>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            List<Order> orders = responseEntity.getBody();
            for (Order order : orders) {
                System.out.println(order.toString());
            }
            System.out.println("OK:display_completed");
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR:store_identifier_does_not_exist");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    private static void requestItem(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String orderID = tokens[2];
            String itemName = tokens[3];
            int quantity = Integer.parseInt(tokens[4]);
            int unitPrice = Integer.parseInt(tokens[5]);
            String url = baseUrl + "/request_item?storeName=" + storeName + "&orderID=" + orderID + "&itemName=" + itemName + "&quantity=" + quantity + "&unitPrice=" + unitPrice;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println("OK:change_completed");
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    private static void purchaseOrder(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String orderID = tokens[2];
            String url = baseUrl + "/purchase_order?storeName=" + storeName + "&orderID=" + orderID;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println("OK:change_completed");
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void cancelOrder(String command) {
        String[] tokens = command.split(",");
        if (tokens[0].equals("cancel_order")) {
            try {
                String storeName = tokens[1];
                String orderID = tokens[2];
                String url = baseUrl + "/cancel_order?storeName=" + storeName + "&orderID=" + orderID;
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
                System.out.println("OK:change_completed");
            } catch (HttpClientErrorException e) {
                System.out.println(e.getResponseBodyAsString());
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void transferOrder(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String orderID = tokens[2];
            String droneID = tokens[3];
            String url = baseUrl + "/transfer_order?storeName=" + storeName + "&orderID=" + orderID + "&droneID=" + droneID;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println("OK:change_completed");
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void displayEfficiency() {
        String url = baseUrl + "/display_efficiency";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
    }

    private static void makeFuelingStation(String command) {
        String[] tokens = command.split(",");
        try {
            String stationID = tokens[1];
            int xCoordinate = Integer.parseInt(tokens[2]);
            int yCoordinate = Integer.parseInt(tokens[3]);
            String storeName = tokens[4];
            String url = baseUrl + "/store/makeFuelingStation?stationID=" + stationID + "&xCoordinate=" + xCoordinate + "&yCoordinate=" + yCoordinate + "&storeName=" + storeName;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void deleteFuelingStation(String command) {
        String[] tokens = command.split(",");
        try {
            String stationID = tokens[1];
            String url = baseUrl + "/deleteFuelingStation?stationID=" + stationID;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void checkRefuelingOption(String command) {
        String[] tokens = command.split(",");
        try {
            String droneID = tokens[1];
            String storeName = tokens[2];
            String url = baseUrl + "/checkRefuelingOption?droneID=" + droneID + "&storeName=" + storeName;
            RestTemplate restTemplate = new RestTemplate();
            ParameterizedTypeReference<List<FuelingStation>> responseType = new ParameterizedTypeReference<List<FuelingStation>>() {
            };
            ResponseEntity<List<FuelingStation>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            List<FuelingStation> fuelingStations = responseEntity.getBody();
            for (FuelingStation fuelingStation : fuelingStations) {
                System.out.println(fuelingStation.toString());
            }
            System.out.println("OK:display_completed");
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            if (errorMessage.contains("store_identifier_does_not_exist")) {
                System.out.println("ERROR:store_identifier_does_not_exist");
            }
            if (errorMessage.contains("drone_identifier_does_not_exist")) {
                System.out.println("ERROR:drone_identifier_does_not_exist");
            }
            if (errorMessage.contains("ERROR:drone_cannot_arrive_any_stations_or_store")) {
                System.out.println("ERROR:drone_cannot_arrive_any_stations_or_store");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void refuelDrone(String command) {
        String[] tokens = command.split(",");
        try {
            String droneID = tokens[1];
            String stationID = tokens[2];
            String storeName = tokens[3];
            String url = baseUrl + "/refuel?droneID=" + droneID + "&stationID=" + stationID + "&storeName=" + storeName;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void assignCoupon(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String account = tokens[2];
            String couponId = tokens[3];
            int couponReduction = Integer.parseInt(tokens[4]);
            int expiryPeriod = Integer.parseInt(tokens[5]);
            String url = baseUrl + "/store/assignCoupon?storeName=" + storeName + "&account=" + account + "&couponId=" + couponId + "&couponReduction=" + couponReduction + "&expiryPeriod=" + expiryPeriod;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void displayCoupon(String command) {
        String[] tokens = command.split(",");
        try {
            String storeName = tokens[1];
            String url = baseUrl + "/store/display_coupon?storeName=" + storeName;
            RestTemplate restTemplate = new RestTemplate();
            ParameterizedTypeReference<List<Coupon>> responseType = new ParameterizedTypeReference<List<Coupon>>() {
            };
            ResponseEntity<List<Coupon>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            List<Coupon> coupons = responseEntity.getBody();
            for (Coupon coupon : coupons) {
                System.out.println(coupon.toString());
            }
            System.out.println("OK:display_completed");
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            if (errorMessage.contains("store_identifier_does_not_exist")) {
                System.out.println("ERROR:store_identifier_does_not_exist");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void showHelp() {
        System.out.println("\033[1;33m=======================================================\033[0m");
        System.out.println("\033[1;33m|      Welcome to the Grocery Express System!     |\033[0m");
        System.out.println("\033[1;33m=======================================================\033[0m\n");
        System.out.println("\033[1;33mCommands:\033[0m\n");
        System.out.println("\033[1mmake_store,storeName,revenue,xCoordinate,yCoordinate\033[0m  Create a store with the provided name and initial revenue");
        System.out.println("\033[1mdisplay_stores\033[0m          Display all stores");
        System.out.println("\033[1msell_item,storeName,itemName,weight\033[0m  Sell an item at a store with the provided information");
        System.out.println("\033[1mdisplay_items,storeName\033[0m     Display all items sold at a store");
        System.out.println("\033[1mmake_pilot,account,firstName,lastName,phone,taxID,licenseID,experience\033[0m  Create a pilot with the provided information");
        System.out.println("\033[1mdisplay_pilots\033[0m          Display all pilots");
        System.out.println("\033[1mmake_drone,storeName,droneID,capacity,remainNumOfTrips,fuelRate,maxFuelCapacity,currXCoordinate,currYCoordinate\033[0m        Create a drone with the provided information");
        System.out.println("\033[1mdisplay_drones,storeName\033[0m          Display all drones");
        System.out.println("\033[1mfly_drone,storeName,droneID,account\033[0m  Assign a drone pilot to the provided drone");
        System.out.println("\033[1mmake_customer,account,firstName,lastName,phone,rating,credit,xCoordinate,yCoordinate\033[0m      Create a customer with the provided information");
        System.out.println("\033[1mdisplay_customers\033[0m       Display all customers");
        System.out.println("\033[1mstart_order,storeName,orderID,droneID,account\033[0m  Start an order with the provided information");
        System.out.println("\033[1mdisplay_orders,storeName\033[0m          Display all orders");
        System.out.println("\033[1mrequest_item,storeName,orderID,itemName,quantity,unitPrice\033[0m      Request an item for an order");
        System.out.println("\033[1mpurchase_order,storeName,orderID\033[0m    Purchase all requested items for an order");
        System.out.println("\033[1mcancel_order,storeName,orderID\033[0m      Cancel an order");
        System.out.println("\033[1mtransfer_order,storeName,orderID,droneID\033[0m  Transfer an order from one drone to another");
        System.out.println("\033[1mdisplay_efficiency\033[0m      Display the efficiency statistics of each store");
        System.out.println("\033[1mmake_fueling_station,stationID,xCoordinate,yCoordinate,storeName\033[0m      Make a fueling station with the provided information");
        System.out.println("\033[1mdelete_fueling_station,stationID\033[0m      Delete a fueling station with the provided information");
        System.out.println("\033[1mcheck_refueling_option,droneID,storeName\033[0m      Check if refueling options of a drone");
        System.out.println("\033[1mrefuel_drone,droneID,stationID,storeName\033[0m      Refuel a drone at a station");
        System.out.println("\033[1massign_coupon,storeName,account,couponId,couponReduction,expiryPeriod\033[0m      Assign a coupon to a customer");
        System.out.println("\033[1mdisplay_coupon,storeName\033[0m      Display all coupons of a store");
        System.out.println("\033[1;33m=======================================================\033[0m");
    }

}
