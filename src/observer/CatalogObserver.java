package observer;

public interface CatalogObserver {
    void onPriceChanged(String productId, String productName, double oldPrice, double newPrice);
    void onStockChanged(String productId, String productName, int newStock);
}
