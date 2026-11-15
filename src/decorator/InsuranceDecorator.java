package decorator;

import model.Product;

public class InsuranceDecorator extends ProductDecorator {
    public InsuranceDecorator(Product wrappedProduct) {
        super(wrappedProduct);
    }

    @Override
    public String getDescription() {
        return wrappedProduct.getDescription() + " + Shipping Insurance";
    }

    @Override
    public double getPrice() {
        return wrappedProduct.getPrice() + 15.0;
    }
}
