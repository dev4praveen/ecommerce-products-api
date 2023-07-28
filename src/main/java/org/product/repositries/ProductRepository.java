package org.product.repositries;

import org.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusTrueOrderByPostedDateDesc();

    List<Product> findByNameContainingAndPriceBetweenAndPostedDateBetween(
            String productName, double minPrice, double maxPrice,
            LocalDateTime minPostedDate, LocalDateTime maxPostedDate
    );
}
