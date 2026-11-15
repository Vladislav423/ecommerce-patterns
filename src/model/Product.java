package model;

public interface Product {
    String getId();
    String getName();
    String getDescription();
    double getPrice();
    void updateBasePrice(double newPrice);
}
