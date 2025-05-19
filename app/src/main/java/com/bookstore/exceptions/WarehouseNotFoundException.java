package com.bookstore.exceptions;

import java.util.UUID;

public class WarehouseNotFoundException extends WarehouseException {
  public WarehouseNotFoundException(UUID warehouseId) {
    super("Склад с ID " + warehouseId + " не найден");
  }
  public WarehouseNotFoundException(UUID warehouseId, Throwable cause) {
    super("Склад с ID " + warehouseId + " не найден", cause);
  }
}
