package oop;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import designPatterns.singleton.AppConfig;
import designPatterns.singleton.OrderIdGenerator;

import solid.srp.SRPReceiptFormatter;
import solid.srp.SRPReceiptPrinter;
import solid.srp.ReceiptTotals;

import solid.isp.PaymentResult;
import designPatterns.factory.PaymentFactories;
import designPatterns.factory.PaymentFactories.PaymentCreator;

import designPatterns.strategy.DiscountPolicyRegistry;
import designPatterns.strategy.DiscountPolicyRegistry.RegisteredPolicy;

import solid.ocp.DiscountPolicy;
import solid.ocp.NoDiscountPolicy;
import solid.ocp.PercentageDiscountPolicy;
import solid.ocp.DatedHappyHourDiscountPolicy;

/**
 * Restaurant Application
 *
 * Features:
 *  - Menu & Order management
 *  - SRP receipt formatting/printing
 *  - Factory Method for payment
 *  - Discount Policy Registry (create policies outside checkout)
 *  - Checkout selection of: No Discount, Ad-hoc Percentage, or any Registered Policy
 *
 * NOTE:
 *  - Ensure the following exist:
 *      solid/ocp/DiscountPolicy.java
 *      solid/ocp/NoDiscountPolicy.java
 *      solid/ocp/PercentageDiscountPolicy.java
 *      solid/ocp/DatedHappyHourDiscountPolicy.java
 *      designPatterns/strategy/DiscountPolicyRegistry.java
 */
public class RestaurantApp {

    private static final DiscountPolicyRegistry POLICY_REGISTRY = DiscountPolicyRegistry.getInstance();

    public static void main(String[] args) {
        configureApp();

        Menu menu = seedSampleMenu();
        Order order = new Order();
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Restaurant App (Discount Registry + Strategy + Payment Factory) ===");
            System.out.println("1) Manage Menu");
            System.out.println("2) Create/Edit Order");
            System.out.println("3) AddDiscountPolicies");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1":
                    manageMenu(menu, in);
                    break;
                case "2":
                    manageOrder(menu, order, in);
                    break;
                case "3":
                    manageDiscountPolicies(in);
                    break;
                case "0":
                    if (!order.isEmpty()) {
                        System.out.print("You have an active order. Exit anyway? (y/N): ");
                        String ans = in.nextLine().trim().toLowerCase();
                        if (!ans.equals("y")) break;
                    }
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /* ========================= CONFIG ========================= */

    private static void configureApp() {
        AppConfig cfg = AppConfig.getInstance();
        cfg.setTaxRate(0.08);   // Not currently applied
        cfg.setCurrencySymbol("LKR");
    }

    /* ========================= DISCOUNT POLICY MANAGEMENT ========================= */

    private static void manageDiscountPolicies(Scanner in) {
        while (true) {
            System.out.println("\n--- AddDiscountPolicies ---");
            System.out.println("1) List registered discount policies");
            System.out.println("2) Add One-Time Dated Happy Hour Policy");
            System.out.println("3) Remove a policy");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1":
                    listRegisteredPolicies();
                    break;
                case "2":
                    addDatedHappyHourPolicy(in);
                    break;
                case "3":
                    removePolicy(in);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void listRegisteredPolicies() {
        List<RegisteredPolicy> list = POLICY_REGISTRY.list();
        if (list.isEmpty()) {
            System.out.println("No discount policies registered.");
            return;
        }
        System.out.println("\n-- Registered Discount Policies --");
        for (int i = 0; i < list.size(); i++) {
            RegisteredPolicy rp = list.get(i);
            System.out.printf("%d) id=%d name=%s label=%s active=%s percent=%s%n",
                    i + 1,
                    rp.id,
                    rp.name,
                    rp.displayLabel(),
                    rp.isActiveNow(),
                    rp.extractPercent() == null ? "-" : String.format("%.2f", rp.extractPercent()));
        }
    }

    private static void addDatedHappyHourPolicy(Scanner in) {
        try {
            System.out.print("Policy name (label): ");
            String name = in.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name required.");
                return;
            }
            System.out.print("Date (YYYY-MM-DD): ");
            String dateStr = in.nextLine().trim();
            LocalDate date = LocalDate.parse(dateStr);

            LocalTime start = readTime(in, "Start time (HH:MM): ");
            LocalTime end = readTime(in, "End time (HH:MM): ");
            double percent = readDouble(in, "Percent (e.g., 15 for 15%): ");

            DatedHappyHourDiscountPolicy policy =
                    new DatedHappyHourDiscountPolicy(date, start, end, percent);

            POLICY_REGISTRY.add(name, policy);
            System.out.println("Dated Happy Hour Policy registered.");
        } catch (Exception e) {
            System.out.println("Failed to add policy: " + e.getMessage());
        }
    }

    private static void removePolicy(Scanner in) {
        listRegisteredPolicies();
        List<RegisteredPolicy> list = POLICY_REGISTRY.list();
        if (list.isEmpty()) return;
        Integer idx = readInt(in, "Enter list number to remove (or 0 to cancel): ");
        if (idx == null || idx < 0) {
            System.out.println("Cancelled.");
            return;
        }
        if (idx == 0) {
            System.out.println("Cancelled.");
            return;
        }
        if (idx > list.size()) {
            System.out.println("Invalid number.");
            return;
        }
        RegisteredPolicy rp = list.get(idx - 1);
        boolean removed = POLICY_REGISTRY.removeById(rp.id);
        System.out.println(removed ? "Removed." : "Remove failed.");
    }

    /* ========================= MENU MANAGEMENT ========================= */

    private static void manageMenu(Menu menu, Scanner in) {
        while (true) {
            System.out.println("\n--- Menu Management ---");
            System.out.println("1) List items");
            System.out.println("2) Add item");
            System.out.println("3) Remove item");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1":
                    listMenu(menu);
                    break;
                case "2":
                    addMenuItem(menu, in);
                    break;
                case "3":
                    removeMenuItem(menu, in);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void listMenu(Menu menu) {
        List<MenuItem> items = menu.getItems();
        if (items.isEmpty()) {
            System.out.println("Menu is empty.");
            return;
        }
        System.out.println("\n-- MENU --");
        for (MenuItem i : items) System.out.println(i);
    }

    private static void addMenuItem(Menu menu, Scanner in) {
        System.out.print("Name: ");
        String name = in.nextLine();

        System.out.print("Description: ");
        String desc = in.nextLine();

        Double price = readPrice(in, "Price: ");
        if (price == null) return;

        boolean ok = menu.addItem(new MenuItem(name, desc, price));
        System.out.println(ok ? "Item added." : "An item with that name already exists.");
    }

    private static void removeMenuItem(Menu menu, Scanner in) {
        System.out.print("Enter name to remove: ");
        String name = in.nextLine();
        boolean ok = menu.removeByName(name);
        System.out.println(ok ? "Removed." : "No item found with that name.");
    }

    /* ========================= ORDER MANAGEMENT ========================= */

    private static void manageOrder(Menu menu, Order order, Scanner in) {
        while (true) {
            System.out.println("\n--- Customer Order ---");
            System.out.println("1) Show menu");
            System.out.println("2) Create / Add Items");
            System.out.println("3) View current order");
            System.out.println("4) Remove item from order");
            System.out.println("5) Clear order");
            System.out.println("6) Checkout");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1":
                    listMenu(menu);
                    break;
                case "2":
                    createOrAddItems(menu, order, in);
                    break;
                case "3":
                    viewOrder(order);
                    break;
                case "4":
                    removeFromOrder(order, in);
                    break;
                case "5":
                    order.clear();
                    System.out.println("Order cleared.");
                    break;
                case "6":
                    checkout(order, in);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void createOrAddItems(Menu menu, Order order, Scanner in) {
        if (menu.getItems().isEmpty()) {
            System.out.println("Menu is empty, add menu items first.");
            return;
        }

        while (true) {
            System.out.println("\nEnter item (blank to stop adding items).");
            System.out.print("Item name: ");
            String name = in.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Stopped adding items.");
                break;
            }

            MenuItem item = menu.findByName(name);
            if (item == null) {
                System.out.println("No such item on the menu.");
                continue;
            }

            Integer qty = readInt(in, "Quantity: ");
            if (qty == null || qty <= 0) {
                System.out.println("Quantity must be >= 1.");
                continue;
            }

            order.addItem(item, qty);
            System.out.println("Added: " + item.getName() + " x" + qty);

            System.out.print("Add another (a), Checkout (c), or Finish (f/Enter)? [a/c/f]: ");
            String action = in.nextLine().trim().toLowerCase();
            if (action.equals("a")) {
                continue;
            } else if (action.equals("c")) {
                checkout(order, in);
                return;
            } else {
                System.out.println("Finished adding items.");
                break;
            }
        }
    }

    private static void viewOrder(Order order) {
        if (order.isEmpty()) {
            System.out.println("Order is empty.");
            return;
        }

        String currency = AppConfig.getInstance().getCurrencySymbol();
        System.out.println("\n-- CURRENT ORDER --");
        for (OrderLine l : order.getLines()) {
            System.out.printf("%-16s x%-3d = %s%6.2f%n",
                    l.getItem().getName(), l.getQuantity(), currency, l.lineTotal());
        }
        System.out.printf("Subtotal: %s%.2f%n", currency, order.total());
    }

    private static void removeFromOrder(Order order, Scanner in) {
        if (order.isEmpty()) {
            System.out.println("Order is empty.");
            return;
        }
        System.out.print("Enter item name to remove: ");
        String name = in.nextLine().trim();
        boolean removed = order.removeByName(name);
        System.out.println(removed ? "Removed." : "Item not found in order.");
    }

    /* ========================= CHECKOUT ========================= */

    private static void checkout(Order order, Scanner in) {
        if (order.isEmpty()) {
            System.out.println("Order is empty. Nothing to checkout.");
            return;
        }

        long orderId = OrderIdGenerator.getInstance().nextId();
        double subtotal = order.total();

        // Select discount policy and compute totals
        DiscountSelectionResult sel = selectDiscountPolicyAtCheckout(in, subtotal);

        ReceiptTotals totals = new ReceiptTotals(
                subtotal,
                sel.label,
                sel.percent,
                sel.discountAmount,
                sel.finalTotal
        );

        SRPReceiptFormatter formatter = new SRPReceiptFormatter();
        SRPReceiptPrinter printer = new SRPReceiptPrinter();

        System.out.println("\n--- RECEIPT (Order ID: " + orderId + ") ---");
        printer.print(formatter.format(order, totals));

        // Payment (Factory Method)
        PaymentCreator creator = PaymentFactories.choose(in);
        System.out.println("Selected payment: " + creator.label());
        try {
            PaymentResult result = creator.processPayment(sel.finalTotal, in);
            if (result.isSuccess()) {
                System.out.println("Payment successful.");
            } else {
                System.out.println("Payment failed: " + result.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Payment aborted: " + e.getMessage());
        }

        order.clear();
        System.out.println("Order completed. You can start a new one.");
    }

    /**
     * Presents discount choices at checkout:
     *  1) No Discount
     *  2) Ad-hoc Percentage
     *  3+) Registered policies (if any)
     */
    private static DiscountSelectionResult selectDiscountPolicyAtCheckout(Scanner in, double subtotal) {
        List<RegisteredPolicy> reg = POLICY_REGISTRY.list();
        System.out.println("\nSelect Discount Policy:");
        System.out.println("1) No Discount");
        System.out.println("2) Percentage Discount");
        if (!reg.isEmpty()) {
            for (int i = 0; i < reg.size(); i++) {
                RegisteredPolicy rp = reg.get(i);
                System.out.printf("%d) %s%n", i + 3, rp.displayLabel());
            }
        }
        System.out.print("Choice [1]: ");
        String choice = in.nextLine().trim();
        if (choice.isEmpty()) choice = "1";

        try {
            int option = Integer.parseInt(choice);
            if (option == 1) {
                DiscountPolicy policy = new NoDiscountPolicy();
                double finalTotal = policy.apply(subtotal);
                return new DiscountSelectionResult("None", null, subtotal - finalTotal, finalTotal);
            } else if (option == 2) {
                double p = readDouble(in, "Percent (e.g., 10 for 10%): ");
                DiscountPolicy policy = new PercentageDiscountPolicy(p);
                double finalTotal = policy.apply(subtotal);
                double discountAmount = subtotal - finalTotal;
                return new DiscountSelectionResult("Percentage", p, discountAmount, finalTotal);
            } else {
                int idx = option - 3;
                if (idx < 0 || idx >= reg.size()) {
                    System.out.println("Invalid choice -> No Discount.");
                    return new DiscountSelectionResult("None", null, 0.0, subtotal);
                }
                RegisteredPolicy rp = reg.get(idx);
                DiscountPolicy policy = rp.policy;
                double finalTotal = policy.apply(subtotal);
                double discountAmount = subtotal - finalTotal;

                String label = rp.displayLabel();
                Double percent = rp.extractPercent();
                // Hide percent if inactive (to make receipt clear)
                if (discountAmount <= 0 && percent != null && !rp.isActiveNow()) {
                    percent = null;
                }
                return new DiscountSelectionResult(label, percent, discountAmount, finalTotal);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number -> No Discount.");
            return new DiscountSelectionResult("None", null, 0.0, subtotal);
        } catch (Exception e) {
            System.out.println("Error selecting discount (" + e.getMessage() + ") -> No Discount.");
            return new DiscountSelectionResult("None", null, 0.0, subtotal);
        }
    }

    private static class DiscountSelectionResult {
        final String label;
        final Double percent; // nullable
        final double discountAmount;
        final double finalTotal;

        DiscountSelectionResult(String label, Double percent, double discountAmount, double finalTotal) {
            this.label = label;
            this.percent = percent;
            this.discountAmount = discountAmount;
            this.finalTotal = finalTotal;
        }
    }

    /* ========================= UTILITIES ========================= */

    private static Menu seedSampleMenu() {
        Menu menu = new Menu();
        menu.addItem(new MenuItem("Burger", "Beef patty with lettuce", 8.50));
        menu.addItem(new MenuItem("Fries", "Crispy french fries", 3.00));
        menu.addItem(new MenuItem("Cola", "Chilled soda", 2.00));
        return menu;
    }

    private static Double readPrice(Scanner in, String prompt) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        try {
            double p = Double.parseDouble(s);
            if (p < 0) {
                System.out.println("Price must be >= 0.");
                return null;
            }
            return p;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return null;
        }
    }

    private static Integer readInt(Scanner in, String prompt) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return null;
        }
    }

    private static double readDouble(Scanner in, String prompt) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) throw new IllegalArgumentException("Value required");
        return Double.parseDouble(s);
    }

    private static LocalTime readTime(Scanner in, String prompt) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        return LocalTime.parse(s);
    }
}