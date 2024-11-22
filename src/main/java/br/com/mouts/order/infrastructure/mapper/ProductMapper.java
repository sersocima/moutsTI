package br.com.mouts.order.infrastructure.mapper;

import br.com.mouts.order.application.dto.ProductDTO;
import br.com.mouts.order.domain.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDto(Product entity);

    Product toEntity(ProductDTO dto);
}
