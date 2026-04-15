package procedural;

import java.util.*;

public class ProceduralRestaurant {

    // Simple "menu" using a Map: itemName -> price
    private static final Map<String, Double> menu = new LinkedHashMap<>();

    // Simple "order" is a list of item names
    private static final List<String> currentOrder = new ArrayList<>();

    public static void main(String[] args) {
        // 1) Setup menu
        addMenuItem("Burger", 8.50);
        addMenuItem("Fries", 3.00);
        addMenuItem("Cola", 2.00);

        // 2) Take an order (simulated)
        addToOrder("Burger");
        addToOrder("Fries");
        addToOrder("Cola");
        addToOrder("Cola");

        // 3) Calculate bill
        double total = calculateTotal(currentOrder);

        // 4) Print receipt
        printReceipt(currentOrder, total);
    }

    // Procedural helpers
    public static void addMenuItem(String name, double price) {
        menu.put(name, price);
    }

    public static void addToOrder(String itemName) {
        if (!menu.containsKey(itemName)) {
            System.out.println("Item not found in menu: " + itemName);
            return;
        }
        currentOrder.add(itemName);
    }

    public static double calculateTotal(List<String> order) {
        double sum = 0.0;
        for (String item : order) {
            sum += menu.getOrDefault(item, 0.0);
        }
        return sum;
    }

    public static void printReceipt(List<String> order, double total) {
        System.out.println("---- Receipt (Procedural) ----");
        for (String item : order) {
            System.out.printf("%-10s $%.2f%n", item, menu.get(item));
        }
        System.out.println("------------------------------");
        System.out.printf("TOTAL:      $%.2f%n", total);
    }
}