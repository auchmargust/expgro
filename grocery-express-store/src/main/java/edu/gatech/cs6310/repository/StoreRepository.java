package edu.gatech.cs6310.repository;

import edu.gatech.cs6310.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByStoreName(String storeName);

}
