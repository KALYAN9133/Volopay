package Volopay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByDepartmentAndPurchaseDateBetween(String department, Date startDate, Date endDate);
    List<Purchase> findByPurchaseDateBetween(Date startDate, Date endDate);
    List<Purchase> findByPurchaseDateBetweenOrderByPriceDesc(Date startDate, Date endDate);
    List<Purchase> findByPurchaseDateBetweenOrderByQuantityDesc(Date startDate, Date endDate);

}

