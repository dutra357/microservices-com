package com.br.product_api.modules.supplier.repository;

import com.br.product_api.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository <Supplier, Integer> {
}
