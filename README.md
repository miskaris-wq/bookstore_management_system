# 📚 Bookstore Management System

A Java-based application for managing inventory, orders, and logistics in a bookstore.

## 🌟 Features
- **Warehouse & Store Management**
  - Add or remove books
  - Transfer books between warehouse and physical stores
  - Low stock alerts
- **Order Management**
  - Create orders with stock validation
  - Auto price calculation
  - Order status lifecycle: `NEW → PROCESSING → SHIPPED → DELIVERED`
- **Advanced Search**
  - Search by author, title, genre, or ID
  - Filtering by various parameters

## 🛠 Technologies
- **Language**: Java 11+
- **Storage**: In-memory (`Map`, `List`) через `DataSource`
- **Build Tool**: Gradle
- **Security**: Простая аутентификация через Spring Security
- **Templating Engine**: JTE
- **Architecture**:
### 🧱 Application Layers

| Layer          | Description                        | Examples                                                   |
|----------------|------------------------------------|------------------------------------------------------------|
| **Domain**     | Core business entities and logic   | `Order`, `Book`, `Warehouse`                               |
| **DAO**        | Data access and repositories       | `InMemoryBookRepository`, `InMemoryOrderRepository`        |
| **Exceptions** | Custom exception handling          | `InsufficientStockException`, `WarehouseNotFoundException` |
| **DTO**        | Data Transfer Objects              | `BookDTO`, `OrderDTO`, `WarehouseDTO`                      |
| **Mapper**     | DTO ↔ Entity mapping               | `BookMapper`, `OrderMapper`                                |
| **Services**   | Business logic and templates       | `AbstractInventoryService`, `InventoryOperations`          |
| **Controller** | REST and view controllers          | `BookController`, `OrderController`                        |
| **Config**     | General and security configuration | `AppConfig`, `SecurityConfig`                              |


## 🗂 Project Structure

### Domain Models

| Class             | Responsibility                                               |
|-------------------|--------------------------------------------------------------|
| `Book`            | Book entity with validation for price, author, and genre     |
| `Order`           | Represents an order with items, statuses, and price logic    |
| `Warehouse`       | Manages inventory in the warehouse (stock and operations)    |
| `PhysicStore`     | Represents a physical store with its own inventory           |
| `OrderItem`       | Line item in an order: book and quantity                     |
| `InventoryHolder` | Interface implemented by both `Warehouse` and `PhysicStore`  |

### Repositories (DAO)

| Class                           | Responsibility                                                     |
|---------------------------------|--------------------------------------------------------------------|
| `InMemoryBookRepository`        | CRUD operations for books, searching by various criteria           |
| `InMemoryOrderRepository`       | Manages orders, filters by status and customer                     |
| `InMemoryWarehouseRepository`   | Handles book transfers to the warehouse, checks stock availability |
| `InMemoryPhysicStoreRepository` | Manages inventory in physical stores                               |


### Supported Exceptions
- `InsufficientStockException` — Not enough books available for the operation
- `WarehouseNotFoundException` — Warehouse not found
- `PhysicStoreNotFoundException` — Physical store not found
- `LocationNotFoundException` — General error: location not found


### DTO
| Class             | Purpose                                  |
|-------------------|------------------------------------------|
| `BookDTO`         | Book without business logic              |
| `OrderDTO`        | Order information for the client         |
| `OrderItemDTO`    | Individual item in an order              |
| `WarehouseDTO`    | Current state of the warehouse           |
| `PhysicStoreDTO`  | Current state of the physical store      |


### Mappers
| Class               | Purpose                                         |
|---------------------|-------------------------------------------------|
| `BookMapper`        | Converts between `Book` and `BookDTO`           |
| `OrderMapper`       | Converts between `Order` and `OrderDTO`         |
| `OrderItemMapper`   | Converts between `OrderItem` and `OrderItemDTO` |
| `WarehouseMapper`   | Converts warehouse entities to DTOs             |
| `PhysicStoreMapper` | Converts store entities to DTOs                 |


### Utilities & Lookup
| Class               | Purpose                                  |
|---------------------|------------------------------------------|
| `EntityFinder`      | Interface for entity lookup by ID        |
| `BookFinder`        | Finds books                              |
| `OrderFinder`       | Finds orders                             |
| `WarehouseFinder`   | Finds warehouses                         |
| `PhysicStoreFinder` | Finds physical stores                    |


### Services
| Class                      | Responsibility                                               |
|----------------------------|--------------------------------------------------------------|
| `BookService`              | Book management                                              |
| `OrderService`             | Creating, updating, and processing orders                    |
| `InventoryService`         | Template-based access to warehouse and store                 |
| `WarehouseService`         | Specialization for warehouse operations                      |
| `PhysicStoreService`       | Specialization for physical store operations                 |
| `AbstractInventoryService` | Shared inventory logic (Template Method Pattern)             |
| `InventoryOperations`      | Interface for inventory-related operations                   |


### Controllers
| Class                    | Responsibility                                     |
|--------------------------|----------------------------------------------------|
| `BookController`         | Book management (CRUD, search)                     |
| `OrderController`        | API for order operations                           |
| `WarehouseController`    | Warehouse-related operations                       |
| `PhysicStoreController`  | Physical store-related operations                  |
| `AuthController`         | Authentication (login/logout)                      |
| `PageController`         | Pages rendered using JTE templates                 |


### Security
| Class            | Responsibility                           |
|------------------|------------------------------------------|
| `SecurityConfig` | Spring Security configuration            |


### Configuration
| Class         | Responsibility                                        |
|---------------|-------------------------------------------------------|
| `AppConfig`   | Manual component initialization                       |
| `DataSource`  | In-memory data source using `Map`, `List`             |


## 🛠 Installation & Usage

1. **Clone the repository**:
```bash
git clone https://jschool.ifellow.ru/scm/jss2/krasskazova.git
cd krasskazov
 ```
2. **Build the project**:
```bash
./gradlew build
```
3. **Run the application**:
```bash
./gradlew run
```
## 📋 Requirements
- Java 11+
- Gradle build system

## 📦 Main Dependencies
| Category            | Libraries                                                                    |
|---------------------|------------------------------------------------------------------------------|
| **Standard**        | Java SE (including `java.util`, `java.io`, `java.time`, etc.)                |
| **Spring**          | `spring-web`, `spring-webmvc`, `spring-context`, `spring-security`           |
| **Server/Servlet**  | `javax.servlet-api`                                                          |
| **JSON**            | `jackson-databind`                                                           |
| **Logging**         | `slf4j-api`, `logback-classic`                                               |
| **Testing**         | `junit-jupiter`, `mockito-core`, `assertj-core`, `wiremock`, `spring-test`   |
| **Gradle Plugins**  | `checkstyle`, `jacoco`, `dependency-management`, `versions`                  |

## 👥 Author
Ksenya Rasskazova — core system design and implementation