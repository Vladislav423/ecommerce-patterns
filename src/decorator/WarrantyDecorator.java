package decorator;

import model.Product;

public class WarrantyDecorator extends ProductDecorator {
    public WarrantyDecorator(Product wrappedProduct) {
        super(wrappedProduct);
    }

    @Override
    public String getDescription() {
        return wrappedProduct.getDescription() + " + Extended Warranty";
    }

    @Override
    public double getPrice() {
        return wrappedProduct.getPrice() + 45.0;
    }
}
