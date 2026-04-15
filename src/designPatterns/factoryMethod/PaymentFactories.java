package designPatterns.factory;

import solid.isp.PaymentCharge;
import solid.isp.PaymentMethod;
import solid.isp.PaymentResult;

import java.util.Scanner;

/**
 * Single-file minimized Factory Method setup.
 *
 * Contains:
 *  - Abstract PaymentCreator (defines factory method create()).
 *  - Two concrete creators as static nested classes:
 *        CashCreator
 *        CardCreator
 *  - A static utility method choose(...) to select the creator.
 *
 * This reduces file count while still demonstrating Factory Method.
 */
public final class PaymentFactories {

    private PaymentFactories() { }

    /**
     * Abstract Creator in the Factory Method pattern.
     */
    public static abstract class PaymentCreator {
        // Factory Method
        protected abstract PaymentCharge create(Scanner in);

        // Optional template method to standardize payment processing
        public PaymentResult processPayment(double amount, Scanner in) {
            PaymentCharge charge = create(in);
            return charge.charge(amount);
        }

        public abstract String label();
    }

    /**
     * Concrete Creator: Cash
     */
    public static final class CashCreator extends PaymentCreator {
        @Override
        protected PaymentCharge create(Scanner in) {
            // No extra input needed for cash
            return PaymentMethod.cash();
        }

        @Override
        public String label() {
            return "Cash";
        }
    }

    /**
     * Concrete Creator: Card
     */
    public static final class CardCreator extends PaymentCreator {
        @Override
        protected PaymentCharge create(Scanner in) {
            System.out.print("Enter card number: ");
            String cardNo = in.nextLine().trim();
            System.out.print("Enter cardholder name: ");
            String holder = in.nextLine().trim();
            if (cardNo.isEmpty() || holder.isEmpty()) {
                throw new IllegalArgumentException("Card number and holder name are required.");
            }
            return PaymentMethod.card(cardNo, holder);
        }

        @Override
        public String label() {
            return "Card";
        }
    }

    /**
     * Helper to choose a concrete creator (replaces a separate selector class).
     */
    public static PaymentCreator choose(Scanner in) {
        System.out.println("Select payment method:");
        System.out.println("  1) Cash");
        System.out.println("  2) Card");
        System.out.print("Choice [1]: ");
        String choice = in.nextLine().trim();
        if (choice.isEmpty() || "1".equals(choice)) {
            return new CashCreator();
        }
        if ("2".equals(choice)) {
            return new CardCreator();
        }
        System.out.println("Invalid choice. Defaulting to Cash.");
        return new CashCreator();
    }
}