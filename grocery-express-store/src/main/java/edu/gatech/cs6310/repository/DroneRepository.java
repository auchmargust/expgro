package edu.gatech.cs6310.repository;

import edu.gatech.cs6310.domain.Drone;
import edu.gatech.cs6310.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Drone findByDroneID(String droneID);

    Drone findByStoreAndDroneID(Store store, String droneID);

    List<Drone> findByStore(Store store);
}
