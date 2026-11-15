package builder;

import model.Product;

public class CustomLaptop implements Product {
    private String id;
    private String name;
    private double basePrice;
    private String cpu;
    private int ramGb;
    private int storageGb;
    private String gpu;
    private String color;

    private CustomLaptop(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.basePrice = builder.basePrice;
        this.cpu = builder.cpu;
        this.ramGb = builder.ramGb;
        this.storageGb = builder.storageGb;
        this.gpu = builder.gpu;
        this.color = builder.color;
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
        return name + " (" + cpu + ", " + ramGb + "GB RAM, " + storageGb + "GB SSD, " + gpu + ", " + color + ")";
    }

    @Override
    public double getPrice() {
        return basePrice;
    }

    @Override
    public void updateBasePrice(double newPrice) {
        this.basePrice = newPrice;
    }

    public static class Builder {
        private String id;
        private String name;
        private double basePrice;
        private String cpu = "Intel i5";
        private int ramGb = 16;
        private int storageGb = 512;
        private String gpu = "Integrated";
        private String color = "Silver";

        public Builder(String id, String name, double basePrice) {
            this.id = id;
            this.name = name;
            this.basePrice = basePrice;
        }

        public Builder cpu(String cpu, double extraCost) {
            this.cpu = cpu;
            this.basePrice += extraCost;
            return this;
        }

        public Builder ramGb(int ramGb, double extraCost) {
            this.ramGb = ramGb;
            this.basePrice += extraCost;
            return this;
        }

        public Builder storageGb(int storageGb, double extraCost) {
            this.storageGb = storageGb;
            this.basePrice += extraCost;
            return this;
        }

        public Builder gpu(String gpu, double extraCost) {
            this.gpu = gpu;
            this.basePrice += extraCost;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public CustomLaptop build() {
            return new CustomLaptop(this);
        }
    }
}
