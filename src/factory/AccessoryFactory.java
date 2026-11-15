package factory;

import model.Product;

public class AccessoryFactory extends ProductFactory {
    @Override
    public Product createProduct(String id, String name, double price, String detail) {
        return new AccessoryProduct(id, name, price, detail);
    }
}
