package eoin.webdev;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {
    List<PurchaseProduct> findByPurchase_Purchasecustomer(Customer customer);
}
