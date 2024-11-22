package br.com.mouts.order.application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class IncomingItemDTO {

    private Long productId;

    private Integer quantity;

    private BigDecimal unitValue;
}
