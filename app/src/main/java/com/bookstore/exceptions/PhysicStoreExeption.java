package com.bookstore.exceptions;

public class PhysicStoreExeption extends Exception {
    public PhysicStoreExeption(String message) {
        super(message);
    }

    public PhysicStoreExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
