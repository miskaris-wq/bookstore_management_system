package com.bookstore.domain;
import org.springframework.stereotype.Component;

@Component
public enum OrderStatus {
    NEW,           // Новый заказ
    PROCESSING,    // В обработке
    PROCESSED,     // Обработан
    SHIPPED,       // Отправлен
    DELIVERED,     // Доставлен
    CANCELLED      // Отменен
}
