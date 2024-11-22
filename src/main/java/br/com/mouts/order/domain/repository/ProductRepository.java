package br.com.mouts.order.domain.repository;

import br.com.mouts.order.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
