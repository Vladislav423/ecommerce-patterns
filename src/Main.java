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
                    Integer qty = readInt(scanner, "Enter quantity: ");
                    if (qty != null) {
                        shop.addCatalogItemToCart(prodId, qty);
                    }
                    break;
                case "3":
                    System.out.print("Enter custom laptop ID (e.g. L1): ");
                    String lapId = scanner.nextLine().trim();
                    if (!shop.canCreateCustomLaptop(lapId)) {
                        break;
                    }

                    System.out.print("CPU (1 - Intel i5 +$0, 2 - Intel i7 +$200, 3 - M3 Max +$400): ");
                    String cpuOpt = scanner.nextLine().trim();
                    String cpu = "Intel i5";
                    double cpuCost = 0;
                    if (cpuOpt.equals("2")) {
                        cpu = "Intel i7";
                        cpuCost = 200;
                    } else if (cpuOpt.equals("3")) {
                        cpu = "M3 Max";
                        cpuCost = 400;
                    }

                    System.out.print("RAM (1 - 16GB +$0, 2 - 32GB +$150): ");
                    String ramOpt = scanner.nextLine().trim();
                    int ram = ramOpt.equals("2") ? 32 : 16;
                    double ramCost = ramOpt.equals("2") ? 150 : 0;

                    System.out.print("SSD (1 - 512GB +$0, 2 - 1024GB +$120): ");
                    String ssdOpt = scanner.nextLine().trim();
                    int ssd = ssdOpt.equals("2") ? 1024 : 512;
                    double ssdCost = ssdOpt.equals("2") ? 120 : 0;

                    System.out.print("GPU (1 - Integrated +$0, 2 - RTX 4060 +$300): ");
                    String gpuOpt = scanner.nextLine().trim();
                    String gpu = gpuOpt.equals("2") ? "RTX 4060" : "Integrated";
                    double gpuCost = gpuOpt.equals("2") ? 300 : 0;

                    System.out.print("Color: ");
                    String color = scanner.nextLine().trim();

                    shop.addCustomLaptopToCart(lapId, cpu, cpuCost, ram, ramCost, ssd, ssdCost, gpu, gpuCost, color);
                    break;
                case "4":
                    shop.showCart();
                    if (shop.isCartEmpty()) {
                        break;
                    }
                    System.out.print("Enter product ID in cart to decorate: ");
                    String targetId = scanner.nextLine().trim();
                    System.out.print("Addon type (GIFT / WARRANTY / INSURANCE): ");
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
                    System.out.print("Enter product ID: ");
                    String obsId = scanner.nextLine().trim();
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
            return Double.parseDouble(input);
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
