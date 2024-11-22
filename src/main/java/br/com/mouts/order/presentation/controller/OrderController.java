package br.com.mouts.order.presentation.controller;

import br.com.mouts.order.application.dto.OrderDTO;
import br.com.mouts.order.application.dto.OrderStatusUpdateDTO;
import br.com.mouts.order.application.service.OrderService;
import br.com.mouts.order.infrastructure.kafka.OrderConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderConsumer orderConsumer;

    @GetMapping
    public Page<OrderDTO> getOrders(
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "id") Pageable pageable) {
        return orderService.findAllByStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable("id") Long id) {
        return orderService.getOrderById(id);
    }

    @PatchMapping("/{id}/update-status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestBody OrderStatusUpdateDTO statusUpdateDTO) {
        orderService.updateOrderStatus(id, statusUpdateDTO.getStatus());
        return ResponseEntity.noContent().build();
    }
}
