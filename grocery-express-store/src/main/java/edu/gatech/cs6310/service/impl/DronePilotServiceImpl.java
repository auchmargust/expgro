package edu.gatech.cs6310.service.impl;

import edu.gatech.cs6310.domain.DronePilot;
import edu.gatech.cs6310.repository.DronePilotRepository;
import edu.gatech.cs6310.service.DronePilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DronePilotServiceImpl implements DronePilotService {

    @Autowired
    private DronePilotRepository dronePilotRepository;

    @Override
    public void createDronePilot(String account, String firstName, String lastName, String phone, String taxID, String licenseId, int numOfSuccDel) {
        DronePilot existingDronePilot = dronePilotRepository.findByAccount(account);
        if (existingDronePilot != null) {
            throw new IllegalArgumentException("ERROR:pilot_identifier_already_exists");
        }
        DronePilot existingDronePilot2 = dronePilotRepository.findByLicenseId(licenseId);
        if (existingDronePilot2 != null) {
            throw new IllegalArgumentException("ERROR:pilot_license_already_exists");
        }
        DronePilot dronePilot = new DronePilot(account, firstName, lastName, phone, taxID, licenseId, numOfSuccDel);
        dronePilotRepository.save(dronePilot);
    }

    @Override
    public DronePilot findByAccount(String account) {
        return dronePilotRepository.findByAccount(account);
    }

    @Override
    public List<DronePilot> getAllPilots() {
        return dronePilotRepository.findAll();
    }

}


