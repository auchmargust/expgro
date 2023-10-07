package edu.gatech.cs6310.repository;

import edu.gatech.cs6310.domain.DronePilot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DronePilotRepository extends JpaRepository<DronePilot, Long> {
    DronePilot findByAccount(String account);

    DronePilot findByLicenseId(String licenseId);
}
