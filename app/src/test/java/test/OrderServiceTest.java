package test;

import com.bookstore.dao.api.OrderRepository;
import com.bookstore.domain.Order;
import com.bookstore.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        order = new Order(
                "John Moore",
                "123 Main St"
        );
    }

    @Test
    void findOrderById_ShouldReturnOrder_WhenOrderExists() {
        when(orderRepository.findById(orderId)).thenReturn(List.of(order));
        Optional<Order> result = orderService.findOrderById(orderId);
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    void findOrderById_ShouldReturnEmpty_WhenOrderDoesNotExist() {
        when(orderRepository.findById(orderId)).thenReturn(List.of());
        Optional<Order> result = orderService.findOrderById(orderId);
        assertTrue(result.isEmpty());
    }

    @Test
    void findOrderById_ShouldThrowException_WhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> orderService.findOrderById(null));
    }

    @Test
    void findOrdersByCustomer_ShouldReturnOrders_WhenCustomerExists() {
        String customer = "customer@example.com";
        when(orderRepository.findByCustomer(customer)).thenReturn(List.of(order));
        List<Order> result = orderService.findOrdersByCustomer(customer);
        assertEquals(1, result.size());
        assertEquals(order, result.getFirst());
    }

    @Test
    void findOrdersByCustomer_ShouldThrowException_WhenCustomerIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> orderService.findOrdersByCustomer(""));
    }

    @Test
    void saveOrder_ShouldCallRepositoryAdd() {
        orderService.saveOrder(order);
        verify(orderRepository, times(1)).add(order);
    }

    @Test
    void saveOrder_ShouldThrowException_WhenOrderIsNull() {
        assertThrows(NullPointerException.class, () -> orderService.saveOrder(null));
    }
}
