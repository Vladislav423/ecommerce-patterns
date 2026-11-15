package decorator;

import model.Product;

public class GiftWrapDecorator extends ProductDecorator {
    public GiftWrapDecorator(Product wrappedProduct) {
        super(wrappedProduct);
    }

    @Override
    public String getDescription() {
        return wrappedProduct.getDescription() + " + Gift Wrap";
    }

    @Override
    public double getPrice() {
        return wrappedProduct.getPrice() + 5.0;
    }
}
