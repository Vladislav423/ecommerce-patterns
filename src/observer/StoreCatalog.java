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
        if (!observers.contains(observer)) {
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

    public void registerProduct(Product product, int initialStock) {
        products.put(product.getId(), product);
        stock.put(product.getId(), initialStock);
    }

    public Product getProduct(String productId) {
        return products.get(productId);
    }

    public int getStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public Map<String, Product> getAllProducts() {
        return products;
    }

    public void updatePrice(String productId, double newPrice) {
        Product product = products.get(productId);
        if (product != null) {
            double oldPrice = product.getPrice();
            product.updateBasePrice(newPrice);
            notifyPriceChanged(productId, product.getName(), oldPrice, newPrice);
        }
    }

    public void updateStock(String productId, int newStock) {
        Product product = products.get(productId);
        if (product != null) {
            stock.put(productId, newStock);
            notifyStockChanged(productId, product.getName(), newStock);
        }
    }

    public void decreaseStock(String productId, int quantity) {
        int current = getStock(productId);
        int updated = Math.max(0, current - quantity);
        updateStock(productId, updated);
    }
}
