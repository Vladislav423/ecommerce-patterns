package factory;

import model.Product;

public class ElectronicsProduct implements Product {
    private String id;
    private String name;
    private double basePrice;
    private String brand;

    public ElectronicsProduct(String id, String name, double basePrice, String brand) {
        this.id = id;
        this.name = name;
        this.basePrice = Math.max(0, basePrice);
        this.brand = brand;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return name + " (" + brand + ")";
    }

    @Override
    public double getPrice() {
        return basePrice;
    }

    @Override
    public void updateBasePrice(double newPrice) {
        if (newPrice > 0) {
            this.basePrice = newPrice;
        }
    }
}
