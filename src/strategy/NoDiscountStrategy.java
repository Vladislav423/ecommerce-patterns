package strategy;

import model.CartItem;
import java.util.List;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscount(double subtotal, List<CartItem> items) {
        return 0.0;
    }

    @Override
    public String getStrategyName() {
        return "No Discount";
    }
}
