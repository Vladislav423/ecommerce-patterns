package factory;

import model.Product;

public class AccessoryProduct implements Product {
    private String id;
    private String name;
    private double basePrice;
    private String type;

    public AccessoryProduct(String id, String name, double basePrice, String type) {
        this.id = id;
        this.name = name;
        this.basePrice = Math.max(0, basePrice);
        this.type = type;
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
        return name + " [" + type + "]";
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
