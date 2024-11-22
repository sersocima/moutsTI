package br.com.mouts.order.infrastructure.kafka;

import br.com.mouts.order.application.dto.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "processed-orders";

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(OrderDTO orderDTO) {
        try {
            String message = convertToJson(orderDTO);
            kafkaTemplate.send(TOPIC, message);
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem para o Kafka: " + e.getMessage());
        }
    }

    private String convertToJson(OrderDTO orderDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(orderDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter OrderDTO para JSON", e);
        }
    }
}
