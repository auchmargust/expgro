package edu.gatech.cs6310.service;

import edu.gatech.cs6310.domain.DronePilot;

import java.util.List;

public interface DronePilotService {
    void createDronePilot(String account, String firstName, String lastName, String phone, String taxID, String licenseId, int numOfSuccDel);
    DronePilot findByAccount(String account);
    List<DronePilot> getAllPilots();

}
