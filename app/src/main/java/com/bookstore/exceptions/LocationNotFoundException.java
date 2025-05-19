package com.bookstore.exceptions;

import java.util.UUID;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String message) {
        super(message);
    }

    public LocationNotFoundException(UUID id) {
        super("Inventory location not found with ID: " + id);
    }
}