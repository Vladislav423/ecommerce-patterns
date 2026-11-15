import facade.ECommerceFacade;
import strategy.NoDiscountStrategy;
import strategy.PercentageDiscountStrategy;
import strategy.ThresholdDiscountStrategy;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ECommerceFacade shop = new ECommerceFacade("Alex");

        while (true) {
            System.out.println("\n=== E-Commerce Shopping Cart ===");
            System.out.println("1. Show catalog");
            System.out.println("2. Add catalog item to cart (Factory)");
            System.out.println("3. Build custom laptop (Builder)");
            System.out.println("4. Add gift wrap / warranty / insurance (Decorator)");
            System.out.println("5. Choose discount (Strategy)");
            System.out.println("6. Change catalog price or stock (Observer)");
            System.out.println("7. View cart");
            System.out.println("8. Checkout (Facade)");
            System.out.println("9. Run full demo of all 6 patterns");
            System.out.println("0. Exit");
            System.out.print("Select option: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    shop.showCatalog();
                    break;
                case "2":
                    shop.showCatalog();
                    System.out.print("Enter product ID (e.g. E1, A1): ");
                    String prodId = scanner.nextLine().trim();
                    if (!shop.canOrderCatalogProduct(prodId)) {
                        break;
                    }
                    Integer qty = readInt(scanner, "Enter quantity: ");
                    if (qty != null) {
                        shop.addCatalogItemToCart(prodId, qty);
                    }
                    break;
                case "3":
                    buildCustomLaptopMenu(scanner, shop);
                    break;
                case "4":
                    shop.showCart();
                    if (shop.isCartEmpty()) {
                        break;
                    }
                    System.out.print("Enter product ID in cart to decorate: ");
                    String targetId = scanner.nextLine().trim();
                    if (!shop.isItemInCart(targetId)) {
                        break;
                    }
                    System.out.print("Choose addon (1 - Gift Wrap +$5, 2 - Extended Warranty +$45, 3 - Shipping Insurance +$15): ");
                    String addon = scanner.nextLine().trim();
                    shop.applyAddonToItem(targetId, addon);
                    break;
                case "5":
                    System.out.println("1. No discount");
                    System.out.println("2. 15% Promo discount");
                    System.out.println("3. $100 off on orders over $1000");
                    System.out.print("Choose discount: ");
                    String disc = scanner.nextLine().trim();
                    if (disc.equals("2")) {
                        shop.setDiscountStrategy(new PercentageDiscountStrategy(15));
                    } else if (disc.equals("3")) {
                        shop.setDiscountStrategy(new ThresholdDiscountStrategy(1000, 100));
                    } else if (disc.equals("1")) {
                        shop.setDiscountStrategy(new NoDiscountStrategy());
                    } else {
                        System.out.println("Invalid discount option.");
                    }
                    break;
                case "6":
                    System.out.println("1. Change product price");
                    System.out.println("2. Change product stock");
                    System.out.print("Choose action: ");
                    String obsAct = scanner.nextLine().trim();
                    if (!obsAct.equals("1") && !obsAct.equals("2")) {
                        System.out.println("Invalid action.");
                        break;
                    }
                    shop.showCatalog();
                    System.out.print("Enter product ID: ");
                    String obsId = scanner.nextLine().trim();
                    if (!shop.hasCatalogProduct(obsId)) {
                        break;
                    }
                    if (obsAct.equals("1")) {
                        Double newPrice = readDouble(scanner, "Enter new price: ");
                        if (newPrice != null) {
                            shop.updateCatalogPrice(obsId, newPrice);
                        }
                    } else {
                        Integer newStock = readInt(scanner, "Enter new stock: ");
                        if (newStock != null) {
                            shop.updateCatalogStock(obsId, newStock);
                        }
                    }
                    break;
                case "7":
                    shop.showCart();
                    break;
                case "8":
                    shop.checkout();
                    break;
                case "9":
                    runDemo(shop);
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void buildCustomLaptopMenu(Scanner scanner, ECommerceFacade shop) {
        System.out.print("CPU (1 - Intel i5 +$0, 2 - Intel i7 +$200, 3 - M3 Max +$400): ");
        String cpuOpt = scanner.nextLine().trim();
        String cpu;
        double cpuCost;
        if (cpuOpt.equals("1")) {
            cpu = "Intel i5";
            cpuCost = 0;
        } else if (cpuOpt.equals("2")) {
            cpu = "Intel i7";
            cpuCost = 200;
        } else if (cpuOpt.equals("3")) {
            cpu = "M3 Max";
            cpuCost = 400;
        } else {
            System.out.println("Invalid CPU option.");
            return;
        }

        System.out.print("RAM (1 - 16GB +$0, 2 - 32GB +$150): ");
        String ramOpt = scanner.nextLine().trim();
        int ram;
        double ramCost;
        if (ramOpt.equals("1")) {
            ram = 16;
            ramCost = 0;
        } else if (ramOpt.equals("2")) {
            ram = 32;
            ramCost = 150;
        } else {
            System.out.println("Invalid RAM option.");
            return;
        }

        System.out.print("SSD (1 - 512GB +$0, 2 - 1024GB +$120): ");
        String ssdOpt = scanner.nextLine().trim();
        int ssd;
        double ssdCost;
        if (ssdOpt.equals("1")) {
            ssd = 512;
            ssdCost = 0;
        } else if (ssdOpt.equals("2")) {
            ssd = 1024;
            ssdCost = 120;
        } else {
            System.out.println("Invalid SSD option.");
            return;
        }

        System.out.print("GPU (1 - Integrated +$0, 2 - RTX 4060 +$300): ");
        String gpuOpt = scanner.nextLine().trim();
        String gpu;
        double gpuCost;
        if (gpuOpt.equals("1")) {
            gpu = "Integrated";
            gpuCost = 0;
        } else if (gpuOpt.equals("2")) {
            gpu = "RTX 4060";
            gpuCost = 300;
        } else {
            System.out.println("Invalid GPU option.");
            return;
        }

        System.out.print("Color (1 - Silver, 2 - Space Black, 3 - Midnight Blue): ");
        String colorOpt = scanner.nextLine().trim();
        String color;
        if (colorOpt.equals("1")) {
            color = "Silver";
        } else if (colorOpt.equals("2")) {
            color = "Space Black";
        } else if (colorOpt.equals("3")) {
            color = "Midnight Blue";
        } else {
            System.out.println("Invalid color option.");
            return;
        }

        shop.addCustomLaptopToCart(null, cpu, cpuCost, ram, ramCost, ssd, ssdCost, gpu, gpuCost, color);
    }

    private static Integer readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return null;
        }
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + input);
            return null;
        }
    }

    private static Double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return null;
        }
        String input = scanner.nextLine().trim();
        try {
            double val = Double.parseDouble(input);
            if (!Double.isFinite(val)) {
                System.out.println("Invalid price: " + input);
                return null;
            }
            return val;
        } catch (NumberFormatException e) {
            System.out.println("Invalid price: " + input);
            return null;
        }
    }

    private static void runDemo(ECommerceFacade shop) {
        shop.resetForDemo();
        System.out.println("\n=== DEMO: ALL 6 DESIGN PATTERNS ===");

        System.out.println("\n1) Factory Method: adding catalog items to cart");
        shop.addCatalogItemToCart("E1", 1);
        shop.addCatalogItemToCart("A1", 2);

        System.out.println("\n2) Builder: assembling custom laptop");
        shop.addCustomLaptopToCart("L1", "Intel i7", 200, 32, 150, 1024, 120, "RTX 4060", 300, "Space Black");

        System.out.println("\n3) Decorator: adding warranty and gift wrap to items");
        shop.applyAddonToItem("E1", "WARRANTY");
        shop.applyAddonToItem("L1", "GIFT");
        shop.applyAddonToItem("L1", "INSURANCE");

        System.out.println("\n4) Observer: real-time price drop and stock update");
        shop.updateCatalogPrice("E1", 820.0);
        shop.updateCatalogStock("A1", 2);

        System.out.println("\n5) Strategy: applying 15% promo discount");
        shop.setDiscountStrategy(new PercentageDiscountStrategy(15));

        System.out.println("\n6) Facade: completing checkout");
        shop.checkout();
    }
}
