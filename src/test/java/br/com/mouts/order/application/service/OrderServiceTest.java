package br.com.mouts.order.application.service;

import br.com.mouts.order.application.dto.IncomingOrderDTO;
import br.com.mouts.order.application.dto.OrderDTO;
import br.com.mouts.order.domain.entity.Order;
import br.com.mouts.order.domain.repository.OrderRepository;
import br.com.mouts.order.infrastructure.kafka.OrderProducer;
import br.com.mouts.order.infrastructure.mapper.OrderMapper;
import br.com.mouts.order.shared.constants.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderService orderService;

    private IncomingOrderDTO incomingOrderDTO;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        incomingOrderDTO = new IncomingOrderDTO();

        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalValue(BigDecimal.valueOf(50.0));

        when(orderMapper.fromIncomingOrderDTO(incomingOrderDTO)).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(new OrderDTO());
    }

    @Test
    void testProcessOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderService.processOrder(incomingOrderDTO);

        verify(orderMapper, times(1)).fromIncomingOrderDTO(incomingOrderDTO);
        verify(orderRepository, times(1)).save(order);
        verify(orderProducer, times(1)).sendOrder(any(OrderDTO.class));
    }

    @Test
    void testGetOrderById_WhenFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertEquals(order, result);
    }

    @Test
    void testGetOrderById_WhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(1L, OrderStatus.PROCESSING);

        assertEquals(OrderStatus.PROCESSING, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testFindAllByStatus_WhenInformingStatus() {
        Pageable pageable = Pageable.ofSize(10);
        List<Order> orders = List.of(order);
        Page<Order> page = new PageImpl<>(orders);

        when(orderRepository.findByStatus(OrderStatus.PENDING, pageable)).thenReturn(page);
        when(orderMapper.toDTO(order)).thenReturn(new OrderDTO());

        Page<OrderDTO> result = orderService.findAllByStatus("PENDING", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindAllByStatus_WhenNotInformingStatus() {
        Pageable pageable = Pageable.ofSize(10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(new Order()));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.toDTO(any(Order.class))).thenReturn(new OrderDTO());

        Page<OrderDTO> result = orderService.findAllByStatus(null, pageable);

        assertEquals(1, result.getTotalElements());
    }
}
