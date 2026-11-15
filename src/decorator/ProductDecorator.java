package decorator;

import model.Product;

public abstract class ProductDecorator implements Product {
    protected Product wrappedProduct;

    public ProductDecorator(Product wrappedProduct) {
        this.wrappedProduct = wrappedProduct;
    }

    public Product getWrappedProduct() {
        return wrappedProduct;
    }

    @Override
    public String getId() {
        return wrappedProduct.getId();
    }

    @Override
    public String getName() {
        return wrappedProduct.getName();
    }

    @Override
    public String getDescription() {
        return wrappedProduct.getDescription();
    }

    @Override
    public double getPrice() {
        return wrappedProduct.getPrice();
    }

    @Override
    public void updateBasePrice(double newPrice) {
        wrappedProduct.updateBasePrice(newPrice);
    }
}
