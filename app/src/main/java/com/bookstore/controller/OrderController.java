package com.bookstore.controller;

import com.bookstore.dto.OrderDTO;
import com.bookstore.mapper.impl.OrderMapper;
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
    private final OrderMapper orderMapper;

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    private static final String ADMIN_OR_USER = "hasAnyRole('" + ROLE_ADMIN + "','" + ROLE_USER + "')";
    private static final String USER_ONLY = "hasRole('" + ROLE_USER + "')";

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PreAuthorize(ADMIN_OR_USER)
    @GetMapping("/customer/{name}")
    public List<OrderDTO> getOrdersByCustomer(@PathVariable String name) {
        return orderService.findOrdersByCustomer(name).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize(ADMIN_OR_USER)
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable UUID id) {
        return orderService.findOrderById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }


    @PreAuthorize(USER_ONLY)
    @PostMapping
    public OrderDTO createOrder(@RequestBody OrderDTO dto) {
        var order = orderMapper.fromDTO(dto);
        var saved = orderService.saveOrder(order);
        return orderMapper.toDTO(saved);
    }

    @PreAuthorize(USER_ONLY)
    @PostMapping("/{id}/process")
    public void processOrder(@PathVariable UUID id) {
        var order = orderService.findOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        orderService.processOrder(order);
    }

    @PreAuthorize(ADMIN_OR_USER)
    @PostMapping("/{id}/ship")
    public void shipOrder(@PathVariable UUID id) {
        var order = orderService.findOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        orderService.shipOrder(order);
    }

    @PreAuthorize(ADMIN_OR_USER)
    @PostMapping("/{id}/deliver")
    public void deliverOrder(@PathVariable UUID id) {
        var order = orderService.findOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        orderService.deliverOrder(order);
    }

    @PreAuthorize(ADMIN_OR_USER)
    @PostMapping("/{id}/cancel")
    public void cancelOrder(@PathVariable UUID id) {
        var order = orderService.findOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        orderService.cancelOrder(order);
    }

}