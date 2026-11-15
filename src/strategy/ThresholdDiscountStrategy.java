package strategy;

import model.CartItem;
import java.util.List;

public class ThresholdDiscountStrategy implements DiscountStrategy {
    private double minTotal;
    private double discountAmount;

    public ThresholdDiscountStrategy(double minTotal, double discountAmount) {
        this.minTotal = Math.max(0, minTotal);
        this.discountAmount = Math.max(0, discountAmount);
    }

    @Override
    public double calculateDiscount(double subtotal, List<CartItem> items) {
        if (subtotal >= minTotal) {
            return Math.min(subtotal, discountAmount);
        }
        return 0.0;
    }

    @Override
    public String getStrategyName() {
        return "$" + discountAmount + " off on orders over $" + minTotal;
    }
}
