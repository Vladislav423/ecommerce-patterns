package observer;

public interface CatalogSubject {
    void subscribe(CatalogObserver observer);
    void unsubscribe(CatalogObserver observer);
    void notifyPriceChanged(String productId, String productName, double oldPrice, double newPrice);
    void notifyStockChanged(String productId, String productName, int newStock);
}
