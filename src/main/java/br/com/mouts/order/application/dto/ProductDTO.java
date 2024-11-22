package br.com.mouts.order.application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductDTO {

    private Long id;

    private String name;

    private String description;
}
