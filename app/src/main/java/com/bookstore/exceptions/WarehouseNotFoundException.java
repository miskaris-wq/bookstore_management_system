package com.bookstore.exceptions;

import java.util.UUID;

public class WarehouseNotFoundException extends WarehouseException {
  public WarehouseNotFoundException(UUID warehouseId) {
    super("Warehouse with ID " + warehouseId + " not found");
  }
  public WarehouseNotFoundException(UUID warehouseId, Throwable cause) {
    super("Warehouse with ID " + warehouseId + " not found", cause);
  }
}
