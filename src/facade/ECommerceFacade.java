package facade;

import builder.CustomLaptop;
import decorator.GiftWrapDecorator;
import decorator.InsuranceDecorator;
import decorator.WarrantyDecorator;
import factory.AccessoryFactory;
import factory.ElectronicsFactory;
import factory.ProductFactory;
import model.CartItem;
import model.Product;
import observer.CustomerAlertObserver;
import observer.InventoryAuditObserver;
import observer.ShoppingCart;
import observer.StoreCatalog;
import strategy.DiscountStrategy;
import strategy.NoDiscountStrategy;

public class ECommerceFacade {
    private StoreCatalog catalog;
    private ShoppingCart cart;
    private DiscountStrategy discountStrategy;
    private ProductFactory electronicsFactory;
    private ProductFactory accessoryFactory;

    public ECommerceFacade(String customerName) {
        this.catalog = new StoreCatalog();
        this.cart = new ShoppingCart();
        this.discountStrategy = new NoDiscountStrategy();
        this.electronicsFactory = new ElectronicsFactory();
        this.accessoryFactory = new AccessoryFactory();

        catalog.subscribe(cart);
        catalog.subscribe(new CustomerAlertObserver(customerName));
        catalog.subscribe(new InventoryAuditObserver());

        initDefaultCatalog();
    }

    private void initDefaultCatalog() {
        Product phone = electronicsFactory.createProduct("E1", "iPhone 16", 900.0, "Apple");
        Product monitor = electronicsFactory.createProduct("E2", "Odyssey Monitor", 350.0, "Samsung");
        Product mouse = accessoryFactory.createProduct("A1", "MX Master Mouse", 100.0, "Wireless");
        Product headphones = accessoryFactory.createProduct("A2", "WH-1000XM5", 300.0, "Noise Cancelling");

        catalog.registerProduct(phone, 5);
        catalog.registerProduct(monitor, 4);
        catalog.registerProduct(mouse, 10);
        catalog.registerProduct(headphones, 6);
    }

    public void showCatalog() {
        System.out.println("\n--- Store Catalog ---");
        for (Product p : catalog.getAllProducts().values()) {
            System.out.println(p.getId() + " | " + p.getDescription() + " | $" + p.getPrice() + " | Stock: " + catalog.getStock(p.getId()));
        }
    }

    public void addCatalogItemToCart(String productId, int quantity) {
        Product product = catalog.getProduct(productId);
        if (product == null) {
            System.out.println("Product not found: " + productId);
            return;
        }
        int available = catalog.getStock(productId);
        if (available < quantity) {
            System.out.println("Not enough stock for " + product.getName() + ". Available: " + available);
            return;
        }
        cart.addItem(product, quantity);
        System.out.println("Added to cart: " + product.getName() + " x" + quantity);
    }

    public void addCustomLaptopToCart(String id, String cpu, double cpuCost, int ram, double ramCost, int ssd, double ssdCost, String gpu, double gpuCost, String color) {
        CustomLaptop laptop = new CustomLaptop.Builder(id, "Custom Laptop", 800.0)
                .cpu(cpu, cpuCost)
                .ramGb(ram, ramCost)
                .storageGb(ssd, ssdCost)
                .gpu(gpu, gpuCost)
                .color(color)
                .build();

        catalog.registerProduct(laptop, 3);
        cart.addItem(laptop, 1);
        System.out.println("Built and added custom laptop: " + laptop.getDescription() + " ($" + laptop.getPrice() + ")");
    }

    public void applyAddonToItem(String productId, String addonType) {
        CartItem item = cart.findItem(productId);
        if (item == null) {
            System.out.println("Item " + productId + " is not in the cart.");
            return;
        }

        Product current = item.getProduct();
        Product decorated;
        switch (addonType.toUpperCase()) {
            case "GIFT":
                decorated = new GiftWrapDecorator(current);
                break;
            case "WARRANTY":
                decorated = new WarrantyDecorator(current);
                break;
            case "INSURANCE":
                decorated = new InsuranceDecorator(current);
                break;
            default:
                System.out.println("Unknown addon type: " + addonType);
                return;
        }

        cart.replaceProduct(productId, decorated);
        System.out.println("Applied addon to " + productId + ": " + decorated.getDescription() + " | New unit price: $" + decorated.getPrice());
    }

    public void setDiscountStrategy(DiscountStrategy strategy) {
        this.discountStrategy = strategy;
        System.out.println("Active discount strategy set to: " + strategy.getStrategyName());
    }

    public void updateCatalogPrice(String productId, double newPrice) {
        catalog.updatePrice(productId, newPrice);
    }

    public void updateCatalogStock(String productId, int newStock) {
        catalog.updateStock(productId, newStock);
    }

    public void showCart() {
        System.out.println("\n--- Shopping Cart ---");
        if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        for (CartItem item : cart.getItems()) {
            System.out.println(item);
        }
        double subtotal = cart.getSubtotal();
        double discount = discountStrategy.calculateDiscount(subtotal, cart.getItems());
        double finalTotal = Math.max(0, subtotal - discount);
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Discount (" + discountStrategy.getStrategyName() + "): -$" + discount);
        System.out.println("Total: $" + finalTotal);
    }

    public void checkout() {
        if (cart.getItems().isEmpty()) {
            System.out.println("Cannot checkout: cart is empty.");
            return;
        }

        System.out.println("\n--- Checkout ---");
        showCart();
        for (CartItem item : new java.util.ArrayList<>(cart.getItems())) {
            catalog.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }
        cart.clear();
        System.out.println("Order completed successfully!");
    }
}
