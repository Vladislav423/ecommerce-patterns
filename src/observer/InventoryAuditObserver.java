package observer;

public class InventoryAuditObserver implements CatalogObserver {
    @Override
    public void onPriceChanged(String productId, String productName, double oldPrice, double newPrice) {
        System.out.println("[Audit Log] Product " + productId + " (" + productName + ") price updated: $" + oldPrice + " -> $" + newPrice);
    }

    @Override
    public void onStockChanged(String productId, String productName, int newStock) {
        System.out.println("[Audit Log] Product " + productId + " (" + productName + ") stock level is now: " + newStock);
    }
}
