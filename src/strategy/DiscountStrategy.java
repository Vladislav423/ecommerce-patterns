package strategy;

import model.CartItem;
import java.util.List;

public interface DiscountStrategy {
    double calculateDiscount(double subtotal, List<CartItem> items);
    String getStrategyName();
}
