package br.com.mouts.order.application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class OrderDTO {

    private Long id;

    private String status;

    private List<OrderItemDTO> itens = new ArrayList<>();

    private BigDecimal totalValue;
}
