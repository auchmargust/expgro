package edu.gatech.cs6310.repository;

import edu.gatech.cs6310.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCouponId(String couponId);
}