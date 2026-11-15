package observer;

public class CustomerAlertObserver implements CatalogObserver {
    private String customerName;

    public CustomerAlertObserver(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public void onPriceChanged(String productId, String productName, double oldPrice, double newPrice) {
        if (newPrice < oldPrice) {
            System.out.println("[Customer Alert -> " + customerName + "] Price drop on " + productName + "! Was $" + oldPrice + ", now $" + newPrice);
        } else {
            System.out.println("[Customer Alert -> " + customerName + "] Price changed for " + productName + ": $" + newPrice);
        }
    }

    @Override
    public void onStockChanged(String productId, String productName, int newStock) {
        if (newStock == 0) {
            System.out.println("[Customer Alert -> " + customerName + "] " + productName + " is out of stock.");
        } else if (newStock <= 3) {
            System.out.println("[Customer Alert -> " + customerName + "] Hurry! Only " + newStock + " left of " + productName);
        }
    }
}
