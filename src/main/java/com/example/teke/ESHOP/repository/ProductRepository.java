package com.example.teke.ESHOP.repository;

import com.example.teke.ESHOP.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {

    Product findByBarcode(String barcode);

    Iterable<Product> findByCategoryName(String categoryName);

    Optional<Product> findById(UUID id);

    List<Product> findByCategoryNameOrBrand(String categoryName, String brand);

    @Modifying
    @Query("UPDATE Product p SET p.stock= p.stock - :quantity WHERE p.id = :productId and p.stock>= :quantity")
    int updateStock(@Param("productId") UUID productId,@Param("quantity") int quantity);


}
