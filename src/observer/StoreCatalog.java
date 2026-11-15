package observer;

import model.Product;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StoreCatalog implements CatalogSubject {
    private Map<String, Product> products = new LinkedHashMap<>();
    private Map<String, Integer> stock = new LinkedHashMap<>();
    private List<CatalogObserver> observers = new ArrayList<>();

    @Override
    public void subscribe(CatalogObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unsubscribe(CatalogObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyPriceChanged(String productId, String productName, double oldPrice, double newPrice) {
        for (CatalogObserver observer : observers) {
            observer.onPriceChanged(productId, productName, oldPrice, newPrice);
        }
    }

    @Override
    public void notifyStockChanged(String productId, String productName, int newStock) {
        for (CatalogObserver observer : observers) {
            observer.onStockChanged(productId, productName, newStock);
        }
    }

    public void clear() {
        products.clear();
        stock.clear();
    }

    public void registerProduct(Product product, int initialStock) {
        if (product == null || initialStock < 0) {
            return;
        }
        products.put(product.getId(), product);
        stock.put(product.getId(), initialStock);
    }

    public Product getProduct(String productId) {
        if (productId == null) {
            return null;
        }
        return products.get(productId.trim().toUpperCase());
    }

    public int getStock(String productId) {
        if (productId == null) {
            return 0;
        }
        return stock.getOrDefault(productId.trim().toUpperCase(), 0);
    }

    public Map<String, Product> getAllProducts() {
        return products;
    }

    public void updatePrice(String productId, double newPrice) {
        Product product = getProduct(productId);
        if (product == null) {
            System.out.println("Product not found: " + productId);
            return;
        }
        if (!Double.isFinite(newPrice) || newPrice <= 0) {
            System.out.println("Invalid price: must be greater than 0.");
            return;
        }
        newPrice = Math.round(newPrice * 100.0) / 100.0;
        double oldPrice = product.getPrice();
        if (Double.compare(oldPrice, newPrice) == 0) {
            System.out.println("Price for " + product.getName() + " is already $" + newPrice);
            return;
        }
        product.updateBasePrice(newPrice);
        notifyPriceChanged(product.getId(), product.getName(), oldPrice, newPrice);
    }

    public void updateStock(String productId, int newStock) {
        Product product = getProduct(productId);
        if (product == null) {
            System.out.println("Product not found: " + productId);
            return;
        }
        if (newStock < 0) {
            System.out.println("Invalid stock: cannot be negative.");
            return;
        }
        int oldStock = getStock(product.getId());
        if (oldStock == newStock) {
            System.out.println("Stock for " + product.getName() + " is already " + newStock);
            return;
        }
        stock.put(product.getId(), newStock);
        notifyStockChanged(product.getId(), product.getName(), newStock);
    }

    public void decreaseStock(String productId, int quantity) {
        if (quantity <= 0) {
            return;
        }
        int current = getStock(productId);
        int updated = Math.max(0, current - quantity);
        updateStock(productId, updated);
    }
}
