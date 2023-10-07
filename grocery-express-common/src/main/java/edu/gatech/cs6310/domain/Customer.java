package edu.gatech.cs6310.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Customer extends User {

    private String account;
    private int rating;
    private int credit;

    private int tempCost;

    private int xCoordinate;
    private int yCoordinate;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("customer")
    private List<Coupon> coupons;

    public Customer(String firstName, String lastName, String phone, String account, int rating, int credit, int xCoordinate, int yCoordinate) {
        super(firstName, lastName, phone);
        this.account = account;
        this.rating = rating;
        this.credit = credit;
        this.tempCost = 0;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public Customer() {

    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCredit() {
        return credit;
    }

    public void setTempCost(int tempCost) {
        this.tempCost = tempCost;
    }

    public int getTempCost() {
        return tempCost;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void assignCoupons(Coupon coupon) {
        coupons.add(coupon);
    }

    public boolean hasCoupon() {
        if (coupons == null) {
            return false;
        }
        for (Coupon coupon : coupons) {
            if (!coupon.isExpiry()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name:").append(firstName).append("_").append(lastName)
                .append(",phone:").append(phone)
                .append(",rating:").append(rating)
                .append(",credit:").append(credit)
                .append(",xCoordinate:").append(xCoordinate)
                .append(",yCoordinate:").append(yCoordinate)
                .append(",hasCoupon:").append(hasCoupon());

        if (hasCoupon()) {
            sb.append(",coupons: ");
            for (Coupon coupon : coupons) {
                sb.append(coupon.getCouponId()).append("_")
                        .append(coupon.getCouponReduction()).append("_")
                        .append(coupon.getExpiryPeriod()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }

        return sb.toString();
    }
}
