package com.bookstore.exceptions;

import java.util.UUID;

public class PhysicStoreNotFoundException extends PhysicStoreExeption {
    public PhysicStoreNotFoundException(UUID storeId) {
      super("Store with ID " + storeId + " not found");
    }
  public PhysicStoreNotFoundException(UUID storeId, Throwable cause) {
    super("Store with ID " + storeId + " not found", cause);
  }
}
