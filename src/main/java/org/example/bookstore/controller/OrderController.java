package org.example.bookstore.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.OrderItemDto;
import org.example.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.example.bookstore.model.User;
import org.example.bookstore.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public OrderDto placeOrder(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateOrderRequestDto requestDto
    ) {
        return orderService.placeOrder(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<OrderDto> getOrdersHistory(
            @AuthenticationPrincipal User user
    ) {
        return orderService.getOrdersHistory(user.getId());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusRequestDto requestDto
    ) {
        return orderService.updateStatus(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemDto> getOrderItems(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user
    ) {
        return orderService.getOrderItems(orderId, user.getId());
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user
    ) {
        return orderService.getOrderItem(orderId, itemId, user.getId());
    }
}
