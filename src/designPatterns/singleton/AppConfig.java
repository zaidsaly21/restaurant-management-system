package designPatterns.singleton;

/**
 * Global application configuration (Singleton via Holder pattern).
 * Fields kept simple: taxRate (0..1) and currencySymbol.
 */
public class AppConfig {

    private double taxRate = 0.0;        // e.g. 0.08 means 8%
    private String currencySymbol = "$"; // default

    private AppConfig() {}

    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return Holder.INSTANCE;
    }

    public double getTaxRate() {
        return taxRate;
    }

    /**
     * @param taxRate between 0 and 1 (e.g., 0.08 for 8%)
     */
    public void setTaxRate(double taxRate) {
        if (taxRate < 0 || taxRate > 1) {
            throw new IllegalArgumentException("Tax rate must be between 0 and 1 (e.g., 0.08 for 8%).");
        }
        this.taxRate = taxRate;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        if (currencySymbol == null || currencySymbol.isBlank()) {
            throw new IllegalArgumentException("Currency symbol cannot be blank.");
        }
        this.currencySymbol = currencySymbol;
    }
}