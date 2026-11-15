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

    public void showCatalog() {
        System.out.println("\n--- Store Catalog ---");
        for (Product p : catalog.getAllProducts().values()) {
            System.out.println(p.getId() + " | " + p.getDescription() + " | $" + p.getPrice() + " | Stock: " + catalog.getStock(p.getId()));
        }
    }

    public void addCatalogItemToCart(String productId, int quantity) {
        if (productId == null || productId.isBlank()) {
            System.out.println("Product ID cannot be empty.");
            return;
        }
        productId = productId.trim().toUpperCase();
        if (quantity <= 0) {
            System.out.println("Invalid quantity: must be greater than 0.");
            return;
        }
        Product product = catalog.getProduct(productId);
        if (product == null) {
            System.out.println("Product not found: " + productId);
            return;
        }
        int available = catalog.getStock(productId);
        CartItem existing = cart.findItem(productId);
        int alreadyInCart = (existing != null) ? existing.getQuantity() : 0;

        if (alreadyInCart + quantity > available) {
            System.out.println("Not enough stock for " + product.getName() + ". Available: " + available + ", already in cart: " + alreadyInCart);
            return;
        }
        cart.addItem(product, quantity);
        System.out.println("Added to cart: " + product.getName() + " x" + quantity);
    }

    public boolean canCreateCustomLaptop(String id) {
        if (id == null || id.isBlank()) {
            System.out.println("Product ID cannot be empty.");
            return false;
        }
        String normalizedId = id.trim().toUpperCase();
        Product existingProduct = catalog.getProduct(normalizedId);
        if (existingProduct != null && !(existingProduct instanceof CustomLaptop)) {
            System.out.println("Product ID " + normalizedId + " belongs to a standard catalog item. Choose another ID.");
            return false;
        }
        if (cart.findItem(normalizedId) != null) {
            System.out.println("Custom laptop with ID " + normalizedId + " is already in the cart.");
            return false;
        }
        return true;
    }

    public void addCustomLaptopToCart(String id, String cpu, double cpuCost, int ram, double ramCost, int ssd, double ssdCost, String gpu, double gpuCost, String color) {
        if (!canCreateCustomLaptop(id)) {
            return;
        }
        String normalizedId = id.trim().toUpperCase();
        if (color == null || color.isBlank()) {
            color = "Silver";
        }

        CustomLaptop laptop = new CustomLaptop.Builder(normalizedId, "Custom Laptop", 800.0)
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
        if (productId == null || productId.isBlank()) {
            System.out.println("Product ID cannot be empty.");
            return;
        }
        productId = productId.trim().toUpperCase();
        CartItem item = cart.findItem(productId);
        if (item == null) {
            System.out.println("Item " + productId + " is not in the cart.");
            return;
        }

        Product current = item.getProduct();
        Product decorated;
        switch (addonType.trim().toUpperCase()) {
            case "GIFT":
                if (hasDecorator(current, GiftWrapDecorator.class)) {
                    System.out.println("Item " + productId + " already has Gift Wrap.");
                    return;
                }
                decorated = new GiftWrapDecorator(current);
                break;
            case "WARRANTY":
                if (hasDecorator(current, WarrantyDecorator.class)) {
                    System.out.println("Item " + productId + " already has Extended Warranty.");
                    return;
                }
                decorated = new WarrantyDecorator(current);
                break;
            case "INSURANCE":
                if (hasDecorator(current, InsuranceDecorator.class)) {
                    System.out.println("Item " + productId + " already has Shipping Insurance.");
                    return;
                }
                decorated = new InsuranceDecorator(current);
                break;
            default:
                System.out.println("Unknown addon type: " + addonType);
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
        if (productId == null || productId.isBlank()) {
            System.out.println("Product ID cannot be empty.");
            return;
        }
        catalog.updatePrice(productId.trim().toUpperCase(), newPrice);
    }

    public void updateCatalogStock(String productId, int newStock) {
        if (productId == null || productId.isBlank()) {
            System.out.println("Product ID cannot be empty.");
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
