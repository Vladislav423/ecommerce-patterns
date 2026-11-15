package factory;

import model.Product;

public class ElectronicsFactory extends ProductFactory {
    @Override
    public Product createProduct(String id, String name, double price, String detail) {
        return new ElectronicsProduct(id, name, price, detail);
    }
}
