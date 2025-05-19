package com.bookstore.exceptions;

import java.util.UUID;

public class PhysicStoreNotFoundException extends PhysicStoreExeption {
    public PhysicStoreNotFoundException(UUID storeId) {
      super("Склад с ID " + storeId + " не найден");
    }
  public PhysicStoreNotFoundException(UUID storeId, Throwable cause) {
    super("Склад с ID " + storeId + " не найден", cause);
  }
}
