package br.com.mouts.order.infrastructure.kafka;

import br.com.mouts.order.application.dto.IncomingOrderDTO;
import br.com.mouts.order.application.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "order-topic", groupId = "order-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        IncomingOrderDTO order = convertJsonToOrder(message);
        processOrder(order);
    }

    private IncomingOrderDTO convertJsonToOrder(String message) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(message, IncomingOrderDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing message: " + message, e);
        }
    }

    private void processOrder(IncomingOrderDTO order) {
        orderService.processOrder(order);
    }
}
