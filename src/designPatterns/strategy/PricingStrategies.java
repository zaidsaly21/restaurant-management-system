package designPatterns.strategy;

import solid.ocp.DiscountPolicy;
import solid.ocp.NoDiscountPolicy;
import solid.ocp.PercentageDiscountPolicy;

import java.util.Scanner;


public final class PricingStrategies {

    public static final String VERSION = "PricingStrategies v3.0 (no HappyHour)";

    private PricingStrategies() {}

    
    public static DiscountPolicy choosePolicy(Scanner in) {
        System.out.println("Choose discount:");
        System.out.println("  1) No Discount");
        System.out.println("  2) Percentage (ad-hoc)");
        System.out.print("Choice [1]: ");
        String choice = in.nextLine().trim();
        if (choice.isEmpty()) choice = "1";
        try {
            switch (choice) {
                case "2":
                    double p = readDouble(in, "Percent (e.g. 10 for 10%): ");
                    return new PercentageDiscountPolicy(p);
                case "1":
                default:
                    return new NoDiscountPolicy();
            }
        } catch (Exception ex) {
            System.out.println("Invalid input (" + ex.getMessage() + "). Using No Discount.");
            return new NoDiscountPolicy();
        }
    }

    /**
     * Build display data after applying a policy to the subtotal.
     */
    public static Selection apply(DiscountPolicy policy, double subtotal) {
        String label = "None";
        Double percent = null;
        if (policy instanceof PercentageDiscountPolicy p) {
            label = "Percentage";
            percent = p.getPercent();
        }
        double finalTotal = policy.apply(subtotal);
        double discountAmount = Math.max(0.0, subtotal - finalTotal);
        return new Selection(label, percent, discountAmount, finalTotal);
    }

    public static final class Selection {
        public final String label;
        public final Double percent;
        public final double discountAmount;
        public final double finalTotal;

        public Selection(String label, Double percent, double discountAmount, double finalTotal) {
            this.label = label;
            this.percent = percent;
            this.discountAmount = discountAmount;
            this.finalTotal = finalTotal;
        }
    }

    private static double readDouble(Scanner in, String prompt) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) throw new IllegalArgumentException("Value required");
        return Double.parseDouble(s);
    }
}