package observer;

import model.CartItem;
import model.Product;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements CatalogObserver {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public boolean replaceProduct(String productId, Product decoratedProduct) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setProduct(decoratedProduct);
                return true;
            }
        }
        return false;
    }

    public CartItem findItem(String productId) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public double getSubtotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    @Override
    public void onPriceChanged(String productId, String productName, double oldPrice, double newPrice) {
        CartItem item = findItem(productId);
        if (item != null) {
            item.getProduct().updateBasePrice(newPrice);
            System.out.println("[Cart Observer] Price updated in cart for " + productName + ": $" + oldPrice + " -> $" + newPrice + " | New cart subtotal: $" + getSubtotal());
        }
    }

    @Override
    public void onStockChanged(String productId, String productName, int newStock) {
        CartItem item = findItem(productId);
        if (item != null) {
            if (newStock == 0) {
                System.out.println("[Cart Observer] Warning: " + productName + " is now OUT OF STOCK!");
            } else if (item.getQuantity() > newStock) {
                item.setQuantity(newStock);
                System.out.println("[Cart Observer] Quantity for " + productName + " adjusted to available stock: " + newStock);
            }
        }
    }
}
