# E-Commerce Shopping Cart

Final project for Software Design Patterns (ShP-2216).
Console application in Java (JDK 17) implementing 6 design patterns.

## Implemented Patterns
1. **Factory Method** (`src/factory`) - creates catalog products (`ElectronicsProduct`, `AccessoryProduct`) via `ElectronicsFactory` and `AccessoryFactory`.
2. **Builder** (`src/builder`) - step-by-step configuration of `CustomLaptop` (CPU, RAM, SSD, GPU, Color).
3. **Decorator** (`src/decorator`) - adds optional services to products (`GiftWrapDecorator`, `WarrantyDecorator`, `InsuranceDecorator`).
4. **Strategy** (`src/strategy`) - discount algorithms (`NoDiscountStrategy`, `PercentageDiscountStrategy`, `ThresholdDiscountStrategy`).
5. **Observer** (`src/observer`) - real-time price and stock updates from `StoreCatalog` to `ShoppingCart`, `CustomerAlertObserver`, and `InventoryAuditObserver`.
6. **Facade** (`src/facade`) - `ECommerceFacade` uniting catalog, cart, discounts, and checkout for `Main.java`.

## How to Run
Open the project in IntelliJ IDEA (JDK 17) and run `src/Main.java`.
