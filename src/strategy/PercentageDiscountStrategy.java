package strategy;

import model.CartItem;
import java.util.List;

public class PercentageDiscountStrategy implements DiscountStrategy {
    private double percent;

    public PercentageDiscountStrategy(double percent) {
        this.percent = percent;
    }

    @Override
    public double calculateDiscount(double subtotal, List<CartItem> items) {
        return subtotal * (percent / 100.0);
    }

    @Override
    public String getStrategyName() {
        return percent + "% Promo Discount";
    }
}
