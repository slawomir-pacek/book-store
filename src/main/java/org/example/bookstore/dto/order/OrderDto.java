package org.example.bookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.example.bookstore.Status;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        Status status
) {
}
