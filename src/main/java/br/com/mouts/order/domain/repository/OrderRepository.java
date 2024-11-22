package br.com.mouts.order.domain.repository;

import br.com.mouts.order.domain.entity.Order;
import br.com.mouts.order.shared.constants.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
