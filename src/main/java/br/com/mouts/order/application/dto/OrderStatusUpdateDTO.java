package br.com.mouts.order.application.dto;

import br.com.mouts.order.shared.constants.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderStatusUpdateDTO {

    @NotNull
    private OrderStatus status;
}
