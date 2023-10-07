package edu.gatech.cs6310.repository;

import edu.gatech.cs6310.domain.FuelingStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelingStationRepository extends JpaRepository<FuelingStation, Long> {
    FuelingStation findByStationID(String stationID);

    void deleteByStationID(String stationID);

    @Query("SELECT MAX(f.stationID) FROM FuelingStation f")
    String findMaxStationID();
}