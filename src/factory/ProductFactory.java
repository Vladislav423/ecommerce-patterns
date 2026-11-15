package factory;

import model.Product;

public abstract class ProductFactory {
    public abstract Product createProduct(String id, String name, double price, String detail);
}
