package edu.gatech.cs6310.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@JsonIgnoreProperties("droneAssigned")
public class DronePilot extends Employee {
    private String account;
    private String licenseId;
    private int numOfSuccDel;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "drone_id")
    private Drone droneAssigned;

    public DronePilot(String account, String firstName, String lastName, String phone, String taxID, String licenseId, int numOfSuccDel) {
        super(firstName, lastName, phone, taxID, 0, 0);
        this.account = account;
        this.licenseId = licenseId;
        this.numOfSuccDel = numOfSuccDel;
    }

    public DronePilot() {
        super();
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setNumOfSuccDel(int numOfSuccDel) {
        this.numOfSuccDel = numOfSuccDel;
    }

    public int getNumOfSuccDel() {
        return numOfSuccDel;
    }

    public void assignDrone(Drone drone) {
        droneAssigned = drone;
    }

    public Drone getDroneAssigned() {
        return droneAssigned;
    }

    public void delDrone() {
        droneAssigned = null;
    }

    @Override
    public String toString() {
        return "name:" + firstName + "_" + lastName + ",phone:" + phone + ",taxID:" + taxID + ",licenseID:" + licenseId + ",experience:" + numOfSuccDel;
    }
}
