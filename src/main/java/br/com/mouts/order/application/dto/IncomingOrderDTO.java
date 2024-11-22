package br.com.mouts.order.application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class IncomingOrderDTO {

    List<IncomingItemDTO> itens = new ArrayList<>();
}
