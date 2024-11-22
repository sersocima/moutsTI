package br.com.mouts.order.infrastructure.mapper;


import br.com.mouts.order.application.dto.IncomingOrderDTO;
import br.com.mouts.order.application.dto.OrderDTO;
import br.com.mouts.order.domain.entity.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    OrderItemMapper ORDER_ITEM_MAPPER_INSTACE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "itens", ignore = true)
    OrderDTO toDTO(Order entity);

    @Mapping(target = "status", expression = "java(br.com.mouts.order.shared.constants.OrderStatus.fromName(dto.getStatus()))")
    @Mapping(target = "itens", ignore = true)
    Order toEntity(OrderDTO dto);

    @AfterMapping
    default void mapOrderItemDTO(@MappingTarget OrderDTO orderDTO, Order order) {
        orderDTO.getItens().addAll(order.getItens().stream().map(ORDER_ITEM_MAPPER_INSTACE::toDTO).toList());
    }

    @Mapping(target = "itens", ignore = true)
    @Mapping(target = "status", expression = "java(br.com.mouts.order.shared.constants.OrderStatus.PENDING)")
    Order fromIncomingOrderDTO(IncomingOrderDTO dto);

    @AfterMapping
    default void mapIncomingOrderItemDTO(IncomingOrderDTO dto, @MappingTarget Order order) {
        dto.getItens().forEach(item -> {
            order.addItem(ORDER_ITEM_MAPPER_INSTACE.fromIncomingItemDTO(item));
        });
        order.calculateTotalValue();
    }
}
