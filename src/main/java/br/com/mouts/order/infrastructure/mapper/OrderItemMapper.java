package br.com.mouts.order.infrastructure.mapper;

import br.com.mouts.order.application.dto.IncomingItemDTO;
import br.com.mouts.order.application.dto.OrderItemDTO;
import br.com.mouts.order.domain.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemDTO toDTO(OrderItem entity);

    @Mapping(target = "product.id", source = "productId")
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "subTotal", expression = "java(dto.getUnitValue().multiply(java.math.BigDecimal.valueOf(dto.getQuantity())))")
    OrderItem fromIncomingItemDTO(IncomingItemDTO dto);
}
