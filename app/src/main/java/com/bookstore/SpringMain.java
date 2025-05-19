package com.bookstore;

import com.bookstore.config.AppConfig;
import com.bookstore.dao.api.BookRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringMain {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            BookRepository repo = context.getBean(BookRepository.class);
            System.out.println("Spring context работает! Репозиторий: " + repo.getClass());
        }
    }
}
