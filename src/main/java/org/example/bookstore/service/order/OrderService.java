package org.example.bookstore.service.order;

import java.util.List;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.OrderItemDto;
import org.example.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto placeOrder(Long userId, CreateOrderRequestDto requestDto);

    Page<OrderDto> getOrdersHistory(Long userId, Pageable pageable);

    OrderDto updateStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemDto> getOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItem(Long orderId, Long itemId, Long userId);
}
