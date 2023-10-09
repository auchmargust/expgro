## Run any of the commands listed:
```
make_store,<Store>,<InitialRevenue>
display_stores
sell_item,<Store>,<Item>,<Weight>
display_items,<Store>
make_pilot,<Account>,<FirstName>,<LastName>,<PhoneNumber>,<TaxId>,<LicenseId>,<ExperienceLevel>
display_pilots
make_drone,<Store>,<DroneId>,<WeightCapacity>,<NumberOfDeliveries>
display_drones,<Store>
fly_drone,<Store>,<DroneId>,<PilotAccount>
make_customer,<Account>,<FirstName>,<LastName>,<PhoneNumber>,<CustomerRating>,<Credits>
display_customers
start_order,<Store>,<OrderId>,<DroneId>,<CustomerAccount>
display_orders,<Store>
request_item,<Store>,<OrderId>,<Item>,<Quantity>,<UnitPrice>
purchase_order,<Store>,<OrderId>
cancel_order,<Store>,<OrderId>
transfer_order,<Store>,<OrderId>,<DroneId>
display_efficiency
stop
```
### Advanced Implementations: Time-Sensitive Coupons

- Time-sensitive coupons are to be instantiated with 3 key attributes: coupon id, coupons reduction which represents the dollar reductions for customer’s order, and coupon expiration date, which sets an important deadline for order delivery.
- The distribution of coupons has been set as a function within time-sensitive coupon class: once distributed, this specific coupon id will be associated with the customer.
- Coupon class has been associated with Customer class via TreeMap as an attribute for coupons received for each customer.
- Coupon class also has a relationship with Store class: Store could add attributes and functions such as distribution frequency which could be adjusted and determine the frequency of calling coupon’s distribution functions. While distributing, Store should also check customer ratings and distribute more frequently to customers with higher ratings.
- Once associated with customer class, coupons could be related to order class. When an order is purchased, the system could check if the corresponding customer has a coupon and the coupon should be applied onto order cost and customer credit deducted. Order should also have an additional attribute such as delivery time to compare with the coupon's expiry date. If the order's delivery time exceeds the coupon's expiry date, it will affect the store's revenue.
- New commands include:

```
[1] assign_coupon,storeName
Command:
assign_coupon,storeName,account,couponId,couponReduction,expiryPeriod

[2] display_coupon
Command:
display_coupon,storeName
```

### Advanced Implementations: Fueling Station

- We have created a new FuelingStation class that includes attributes for StationID and Location. The StationId will be used to uniquely identify each fueling station, and the Location attribute will store the coordinates of the fueling station.
- The FuelingStation class has been associated with the Store class. We have modified the Store class to include a new attribute, FuelingStations, which stores all fueling stations in the system. By default, the Store class is also a fueling station.
- New commands include:

```
[1] make_fueling_station
Command:
make_fueling_station,stationID,xCoordinate,yCoordinate,storeName


[2] delete_fueling_station
Command:
delete_fueling_station,stationID


[3] check_refueling_option
Command:
check_refueling_option,droneID,storeName


[4] refuel_drone
Command:
refuel_drone,droneID,stationID,storeName

```

## Furthur commands with using Docker & Debugging
### To run & test in interactive mode

```
java -jar drone_delivery.jar < commands_00.txt > drone_delivery_00_results.txt
diff -s drone_delivery_00_results.txt drone_delivery_initial_00_results.txt > results_00.txt
cat results_00.txt
```

### To run a specific scenario with your jar and output to localhost
The "mkdir docker_results ; " would not be needed after the 1st run, but just in case you have not made the directory yet with another command. 
```
mkdir docker_results ; docker run --rm gatech/dronedelivery sh -c 'java -jar drone_delivery.jar < commands_00.txt'  > docker_results/drone_delivery_00_results.txt
```

### If you get stuck in an infinite loop
Simply stop and remove the running container
```
docker ps
docker rm -f <container_id>
```

### To test with a clean image & container
After running the below command you will need to run the build command again
#### Windows
```
docker ps -aq | % { docker stop $_ } | % { docker rm -f $_ } | docker images -f "dangling=true" -aq | % { docker rmi -f $_ } | docker images gatech/* -aq | % { docker rmi -f $_ }
```
#### Mac
```
docker ps -aq | xargs docker stop | xargs docker rm -f && docker images -f "dangling=true" -aq | xargs docker rmi -f && docker images "gatech/*" -aq | xargs docker rmi -f
```