package edu.gatech.cs6310.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnoreProperties("coupons")
    private Store store;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("coupons")
    private Customer customer;
    private String couponId;
    private int couponReduction;
    private int expiryPeriod;
    private Calendar expiryDate;
    private boolean expiry;

    public Coupon(Store store, String couponId, int couponReduction, int expiryPeriod) {
        this.store = store;
        this.couponId = couponId;
        this.couponReduction = couponReduction;
        this.expiryPeriod = expiryPeriod;
        this.expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.DAY_OF_MONTH, expiryPeriod);
    }

    public Coupon() {

    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public int getCouponReduction() {
        return couponReduction;
    }

    public void setCouponReduction(int couponReduction) {
        this.couponReduction = couponReduction;
    }

    public int getExpiryPeriod() {
        return expiryPeriod;
    }

    public void setExpiryPeriod(int expiryPeriod) {
        this.expiryPeriod = expiryPeriod;
    }

    public Calendar getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Calendar expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isExpiry() {
        Calendar now = Calendar.getInstance();
        if (expiryDate.compareTo(now) <= 0) {
            expiry = true;
        } else {
            expiry = false;
        }
        return expiry;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                ", store=" + store.getName() +
                ", customer=" + customer.getAccount() +
                ", couponId=" + couponId +
                ", couponReduction=" + couponReduction +
                ", expiryPeriod=" + expiryPeriod +
                ", expiryDate=" + expiryDate.getTime() +
                ", expiry=" + expiry +
                '}';
    }

}