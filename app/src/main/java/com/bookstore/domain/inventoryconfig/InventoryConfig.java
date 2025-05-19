package com.bookstore.domain.inventoryconfig;

import com.bookstore.domain.Book;
import com.bookstore.domain.InventoryHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class InventoryConfig {

    @Bean
    @Scope("prototype")
    public InventoryHolder warehouseInventory() {
        return new InventoryHolder() {
            @Override
            public Map<Book, Integer> getInventory() {
                // Реализация для складов
                return new ConcurrentHashMap<>();
            }
        };
    }

    @Bean
    @Scope("prototype")
    public InventoryHolder storeInventory() {
        return new InventoryHolder() {
            @Override
            public Map<Book, Integer> getInventory() {
                // Реализация для магазинов
                return new ConcurrentHashMap<>();
            }
        };
    }
}
