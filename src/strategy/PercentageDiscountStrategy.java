package strategy;

import model.CartItem;
import java.util.List;

public class PercentageDiscountStrategy implements DiscountStrategy {
    private double percent;

    public PercentageDiscountStrategy(double percent) {
        if (percent < 0) {
            this.percent = 0;
        } else if (percent > 100) {
            this.percent = 100;
        } else {
            this.percent = percent;
        }
    }

    @Override
    public double calculateDiscount(double subtotal, List<CartItem> items) {
        if (subtotal <= 0) {
            return 0.0;
        }
        return Math.round(subtotal * (percent / 100.0) * 100.0) / 100.0;
    }

    @Override
    public String getStrategyName() {
        return percent + "% Promo Discount";
    }
}
