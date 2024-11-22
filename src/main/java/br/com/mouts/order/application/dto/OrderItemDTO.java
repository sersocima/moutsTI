package br.com.mouts.order.application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class OrderItemDTO {

    private Long productId;

    private String productName;

    private Integer quantity;

    private BigDecimal unitValue;

    private BigDecimal subTotal;
}
