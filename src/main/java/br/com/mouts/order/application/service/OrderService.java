package br.com.mouts.order.application.service;

import br.com.mouts.order.application.dto.IncomingOrderDTO;
import br.com.mouts.order.application.dto.OrderDTO;
import br.com.mouts.order.domain.entity.Order;
import br.com.mouts.order.domain.repository.OrderRepository;
import br.com.mouts.order.infrastructure.kafka.OrderProducer;
import br.com.mouts.order.infrastructure.mapper.OrderMapper;
import br.com.mouts.order.shared.constants.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    private final OrderMapper orderMapper;

    private final OrderProducer orderProducer;

    @Transactional
    public void processOrder(IncomingOrderDTO incomingOrderDTO) {
        Order order = orderMapper.fromIncomingOrderDTO(incomingOrderDTO);
        order = saveOrder(order);

        orderProducer.sendOrder(orderMapper.toDTO(order));
    }

    @Cacheable(value = "orders", key = "#id")
    public Order findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public OrderDTO getOrderById(Long id) {
        return orderMapper.toDTO(findById(id));
    }

    @CachePut(value = "orders", key = "#order.id")
    public Order saveOrder(Order order) {
        return repository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Long id, OrderStatus status) {
        Order order = findById(id);
        order.setStatus(status);

        repository.save(order);
    }

    public Page<OrderDTO> findAllByStatus(String status, Pageable pageable) {
        Page<Order> orderList;

        if (status == null || status.trim().isEmpty()) {
            orderList = repository.findAll(pageable);
        } else {
            orderList = repository.findByStatus(OrderStatus.fromName(status), pageable);
        }

        return orderList.map(orderMapper::toDTO);
    }

}
