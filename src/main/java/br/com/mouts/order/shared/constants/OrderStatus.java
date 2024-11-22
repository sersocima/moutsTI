package br.com.mouts.order.shared.constants;

import br.com.mouts.order.shared.exception.OrderStatusNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    PENDING("Aguardando"),
    PROCESSING("Em processamento"),
    COMPLETED("Concluído"),
    CANCELED("Cancelado");

    private final String description;

    public static OrderStatus fromName(String name) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new OrderStatusNotFoundException(name));
    }
}
