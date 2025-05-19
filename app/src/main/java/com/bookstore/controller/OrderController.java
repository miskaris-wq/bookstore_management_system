package com.bookstore.controller;

import com.bookstore.dto.OrderDTO;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.services.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/customer/{name}")
    public List<OrderDTO> getOrdersByCustomer(@PathVariable String name) {
        return orderService.findOrdersByCustomer(name).stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable UUID id) {
        return orderService.findOrderById(id)
                .map(OrderMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public void createOrder(@RequestBody OrderDTO dto) {
        throw new UnsupportedOperationException("Создание заказов не реализовано");
    }
}
