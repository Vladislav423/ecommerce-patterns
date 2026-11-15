package facade;

import builder.CustomLaptop;
import decorator.GiftWrapDecorator;
import decorator.InsuranceDecorator;
import decorator.ProductDecorator;
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

import java.util.ArrayList;
import java.util.List;

public class ECommerceFacade {
    private StoreCatalog catalog;
    private ShoppingCart cart;
    private DiscountStrategy discountStrategy;
    private ProductFactory electronicsFactory;
    private ProductFactory accessoryFactory;
    private int customLaptopCounter = 1;

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

    public void initDefaultCatalog() {
        catalog.clear();
        customLaptopCounter = 1;

        Product phone = electronicsFactory.createProduct("E1", "iPhone 16", 900.0, "Apple");
        Product monitor = electronicsFactory.createProduct("E2", "Odyssey Monitor", 350.0, "Samsung");
        Product mouse = accessoryFactory.createProduct("A1", "MX Master Mouse", 100.0, "Wireless");
        Product headphones = accessoryFactory.createProduct("A2", "WH-1000XM5", 300.0, "Noise Cancelling");

        catalog.registerProduct(phone, 5);
        catalog.registerProduct(monitor, 4);
        catalog.registerProduct(mouse, 10);
        catalog.registerProduct(headphones, 6);
    }

    public void resetForDemo() {
        cart.clear();
        discountStrategy = new NoDiscountStrategy();
        initDefaultCatalog();
    }

    public boolean isCartEmpty() {
        return cart.getItems().isEmpty();
    }

    public boolean hasCatalogProduct(String productId) {
        if (productId == null || productId.isBlank()) {
            System.out.println("Product ID cannot be empty.");
            return false;
        }
        if (catalog.getProduct(productId) == null) {
            System.out.println("Product not found: " + productId.trim().toUpperCase());
            return false;
        }
        return true;
    }

    public boolean canOrderCatalogProduct(String productId) {
        if (!hasCatalogProduct(productId)) {
            return false;
        }
        String normalized = productId.trim().toUpperCase();
        Product product = catalog.getProduct(normalized);
        int available = catalog.getStock(normalized);
        CartItem existing = cart.findItem(normalized);
        int alreadyInCart = (existing != null) ? existing.getQuantity() : 0;
        if (available <= 0 || alreadyInCart >= available) {
            System.out.println("No more stock available for " + product.getName() + " (Stock: " + available + ", in cart: " + alreadyInCart + ").");
            return false;
        }
        return true;
    }

    public boolean isItemInCart(String productId) {
        if (productId == null || productId.isBlank()) {
            System.out.println("Product ID cannot be empty.");
            return false;
        }
        String normalized = productId.trim().toUpperCase();
        if (cart.findItem(normalized) == null) {
            System.out.println("Item " + normalized + " is not in the cart.");
            return false;
        }
        return true;
    }

    public String generateNextLaptopId() {
        while (catalog.getProduct("L" + customLaptopCounter) != null) {
            customLaptopCounter++;
        }
        return "L" + (customLaptopCounter++);
    }

    public void showCatalog() {
        System.out.println("\n--- Store Catalog ---");
        for (Product p : catalog.getAllProducts().values()) {
            System.out.println(p.getId() + " | " + p.getDescription() + " | $" + p.getPrice() + " | Stock: " + catalog.getStock(p.getId()));
        }
    }

    public void addCatalogItemToCart(String productId, int quantity) {
        if (!hasCatalogProduct(productId)) {
            return;
        }
        productId = productId.trim().toUpperCase();
        if (quantity <= 0) {
            System.out.println("Invalid quantity: must be greater than 0.");
            return;
        }
        Product product = catalog.getProduct(productId);
        int available = catalog.getStock(productId);
        CartItem existing = cart.findItem(productId);
        int alreadyInCart = (existing != null) ? existing.getQuantity() : 0;

        if (quantity > available - alreadyInCart) {
            System.out.println("Not enough stock for " + product.getName() + ". Available: " + available + ", already in cart: " + alreadyInCart);
            return;
        }
        cart.addItem(product, quantity);
        System.out.println("Added to cart: " + product.getName() + " x" + quantity);
    }

    public void addCustomLaptopToCart(String id, String cpu, double cpuCost, int ram, double ramCost, int ssd, double ssdCost, String gpu, double gpuCost, String color) {
        String normalizedId = (id == null || id.isBlank()) ? generateNextLaptopId() : id.trim().toUpperCase();

        CustomLaptop laptop = new CustomLaptop.Builder(normalizedId, "Custom Laptop", 800.0)
                .cpu(cpu, cpuCost)
                .ramGb(ram, ramCost)
                .storageGb(ssd, ssdCost)
                .gpu(gpu, gpuCost)
                .color(color)
                .build();

        catalog.registerProduct(laptop, 1);
        cart.addItem(laptop, 1);
        System.out.println("Built and added custom laptop [" + normalizedId + "]: " + laptop.getDescription() + " ($" + laptop.getPrice() + ")");
    }

    public void applyAddonToItem(String productId, String addonType) {
        if (!isItemInCart(productId)) {
            return;
        }
        productId = productId.trim().toUpperCase();
        CartItem item = cart.findItem(productId);

        Product current = item.getProduct();
        Product decorated;
        String type = (addonType == null) ? "" : addonType.trim().toUpperCase();
        switch (type) {
            case "1":
            case "GIFT":
                if (hasDecorator(current, GiftWrapDecorator.class)) {
                    System.out.println("Item " + productId + " already has Gift Wrap.");
                    return;
                }
                decorated = new GiftWrapDecorator(current);
                break;
            case "2":
            case "WARRANTY":
                if (hasDecorator(current, WarrantyDecorator.class)) {
                    System.out.println("Item " + productId + " already has Extended Warranty.");
                    return;
                }
                decorated = new WarrantyDecorator(current);
                break;
            case "3":
            case "INSURANCE":
                if (hasDecorator(current, InsuranceDecorator.class)) {
                    System.out.println("Item " + productId + " already has Shipping Insurance.");
                    return;
                }
                decorated = new InsuranceDecorator(current);
                break;
            default:
                System.out.println("Invalid addon option: " + addonType);
                return;
        }

        cart.replaceProduct(productId, decorated);
        System.out.println("Applied addon to " + productId + ": " + decorated.getDescription() + " | New unit price: $" + decorated.getPrice());
    }

    private boolean hasDecorator(Product product, Class<?> decoratorClass) {
        Product curr = product;
        while (curr instanceof ProductDecorator) {
            if (decoratorClass.isInstance(curr)) {
                return true;
            }
            curr = ((ProductDecorator) curr).getWrappedProduct();
        }
        return false;
    }

    public void setDiscountStrategy(DiscountStrategy strategy) {
        if (strategy == null) {
            return;
        }
        this.discountStrategy = strategy;
        System.out.println("Active discount strategy set to: " + strategy.getStrategyName());
    }

    public void updateCatalogPrice(String productId, double newPrice) {
        if (!hasCatalogProduct(productId)) {
            return;
        }
        catalog.updatePrice(productId.trim().toUpperCase(), newPrice);
    }

    public void updateCatalogStock(String productId, int newStock) {
        if (!hasCatalogProduct(productId)) {
            return;
        }
        catalog.updateStock(productId.trim().toUpperCase(), newStock);
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
        double subtotal = Math.round(cart.getSubtotal() * 100.0) / 100.0;
        double discount = Math.round(discountStrategy.calculateDiscount(subtotal, cart.getItems()) * 100.0) / 100.0;
        double finalTotal = Math.round(Math.max(0, subtotal - discount) * 100.0) / 100.0;
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Discount (" + discountStrategy.getStrategyName() + "): -$" + discount);
        System.out.println("Total: $" + finalTotal);
    }

    public void checkout() {
        if (cart.getItems().isEmpty()) {
            System.out.println("Cannot checkout: cart is empty.");
            return;
        }

        for (CartItem item : cart.getItems()) {
            String id = item.getProduct().getId();
            int available = catalog.getStock(id);
            if (item.getQuantity() <= 0 || item.getQuantity() > available) {
                System.out.println("Checkout failed: insufficient stock for " + item.getProduct().getName() + " (available: " + available + ").");
                return;
            }
        }

        System.out.println("\n--- Checkout ---");
        showCart();

        List<CartItem> purchasedItems = new ArrayList<>(cart.getItems());
        cart.clear();

        for (CartItem item : purchasedItems) {
            catalog.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }
        System.out.println("Order completed successfully!");
    }
}
