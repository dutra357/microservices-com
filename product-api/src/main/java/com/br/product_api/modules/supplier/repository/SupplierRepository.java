package com.br.product_api.modules.supplier.repository;

import com.br.product_api.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository <Supplier, Integer> {

    List<Supplier> findByNameIgnoreCaseContaining(String description);

}
