package solid.isp;

public class PaymentMethod implements PaymentCharge {

    public enum Kind { CASH, CARD }

    private final Kind kind;
    private final String cardNumber;   // only for CARD
    private final String cardholder;   // only for CARD

    private PaymentMethod(Kind kind, String cardNumber, String cardholder) {
        this.kind = kind;
        this.cardNumber = cardNumber;
        this.cardholder = cardholder;
    }

    // Factory for cash
    public static PaymentMethod cash() {
        return new PaymentMethod(Kind.CASH, null, null);
    }

    // Factory for card
    public static PaymentMethod card(String cardNumber, String cardholder) {
        String number = cardNumber == null ? "" : cardNumber.trim();
        String holder = cardholder == null ? "" : cardholder.trim();
        return new PaymentMethod(Kind.CARD, number, holder);
    }

    @Override
    public PaymentResult charge(double amount) {
        if (amount < 0) return PaymentResult.fail("Invalid amount");
        if (kind == Kind.CASH) {
            // Cash: always OK for this demo
            return PaymentResult.ok();
        }
        // Card flow
        PaymentResult v = validateCard();
        if (!v.isSuccess()) return v;

        // Allow $0 (e.g., 100% discount). Otherwise require >= $1.00 for card.
        if (amount > 0 && amount < 1.00) {
            return PaymentResult.fail("Minimum card charge is $1.00");
        }
        // Assume approved for demo purposes
        return PaymentResult.ok();
    }

    private PaymentResult validateCard() {
        if (cardNumber == null || cardNumber.length() < 12 || cardNumber.length() > 19) {
            return PaymentResult.fail("Invalid card number length");
        }
        if (cardholder == null || cardholder.isBlank()) {
            return PaymentResult.fail("Cardholder name required");
        }
        // Skipping Luhn for brevity
        return PaymentResult.ok();
    }

    public Kind kind() { return kind; }
    public String cardNumber() { return cardNumber; }
    public String cardholder() { return cardholder; }
}