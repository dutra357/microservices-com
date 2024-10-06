package com.br.product_api.modules.product.repository;

import com.br.product_api.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository <Product, Integer> {

    List<Product> findBySupplierId (Integer id);

    List<Product> findByCategoryId (Integer id);

    List<Product> findByNameIgnoreCaseContaining(String description);

    Boolean existsByCategoryId (Integer id);

    Boolean existsBySupplierId (Integer id);
}
