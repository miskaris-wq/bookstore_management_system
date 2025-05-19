package com.bookstore.config;

import com.bookstore.dao.api.BookRepository;
import com.bookstore.dao.api.OrderRepository;
import com.bookstore.dao.api.PhysicStoreRepository;
import com.bookstore.dao.api.WarehouseRepository;
import com.bookstore.dao.impl.InMemoryBookRepository;
import com.bookstore.dao.impl.InMemoryOrderRepository;
import com.bookstore.dao.impl.InMemoryPhysicStoreRepository;
import com.bookstore.dao.impl.InMemoryWarehouseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bookstore.datasourse.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new DataSource();
    }

    @Bean
    public BookRepository bookRepository() {
        return new InMemoryBookRepository();
    }

    @Bean
    public OrderRepository orderRepository() {
        return new InMemoryOrderRepository();
    }

    @Bean
    public WarehouseRepository warehouseRepository() {
        return new InMemoryWarehouseRepository();
    }

    @Bean
    public PhysicStoreRepository physicStoreRepository() {
        return new InMemoryPhysicStoreRepository();
    }
}