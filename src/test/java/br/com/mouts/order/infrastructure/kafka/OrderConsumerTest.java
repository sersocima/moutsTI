package br.com.mouts.order.infrastructure.kafka;

import br.com.mouts.order.application.dto.IncomingItemDTO;
import br.com.mouts.order.application.dto.IncomingOrderDTO;
import br.com.mouts.order.application.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

class OrderConsumerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderConsumer orderConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeIncomingOrder() throws Exception {
        String message = "{\"itens\": [{\"productId\": 1, \"quantity\": 2, \"unitValue\": 25.0}]}";

        IncomingOrderDTO incomingOrderDTO = generateIncomingOrderDTO();

        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        Mockito.when(mapper.readValue(message, IncomingOrderDTO.class)).thenReturn(incomingOrderDTO);

        orderConsumer.consume(message);

        Mockito.verify(orderService).processOrder(incomingOrderDTO);
    }

    @Test
    void testConsumeIncomingOrder_WhenIncorrectMessage() throws Exception {
        String message = "{\"iten\": [{\"productId\": 1, \"quantity\": 2, \"unitValue\": 25.0}]}";

        Assertions.assertThrows(RuntimeException.class, () -> orderConsumer.consume(message));
    }

    private IncomingOrderDTO generateIncomingOrderDTO() {
        IncomingItemDTO incomingItemDTO = new IncomingItemDTO();
        incomingItemDTO.setProductId(1L);
        incomingItemDTO.setQuantity(2);
        incomingItemDTO.setUnitValue(BigDecimal.valueOf(25.0));

        IncomingOrderDTO incomingOrderDTO = new IncomingOrderDTO();
        incomingOrderDTO.getItens().add(incomingItemDTO);

        return incomingOrderDTO;
    }
}