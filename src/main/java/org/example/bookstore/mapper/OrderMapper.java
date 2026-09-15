package org.example.bookstore.mapper;

import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.OrderItemDto;
import org.example.bookstore.model.Order;
import org.example.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toOrderItemDto(OrderItem orderItem);
}
