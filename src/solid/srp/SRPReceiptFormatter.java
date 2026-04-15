package solid.srp;

import oop.Order;
import oop.OrderLine;
import designPatterns.singleton.AppConfig;


public class SRPReceiptFormatter {

    // Legacy simple receipt (no discount details, no tax) but now uses currency symbol from AppConfig
    public String format(Order order) {
        AppConfig cfg = AppConfig.getInstance();
        String currency = cfg.getCurrencySymbol();

        StringBuilder sb = new StringBuilder();
        sb.append("========= RECEIPT (SRP) =========\n");
        for (OrderLine l : order.getLines()) {
            String name = l.getItem().getName();
            double price = l.getItem().getPrice();
            int   qty   = l.getQuantity();
            double line = l.lineTotal();
            sb.append(String.format("%-16s x%-3d @ %s%5.2f  = %s%6.2f%n",
                    name, qty, currency, price, currency, line));
        }
        sb.append("---------------------------------\n");
        sb.append(String.format("SUBTOTAL:          %s%6.2f%n", currency, order.total()));
        sb.append("=================================\n");
        return sb.toString();
    }

    
    public String format(Order order, ReceiptTotals totals) {
        AppConfig cfg = AppConfig.getInstance();
        String currency = cfg.getCurrencySymbol();
        double taxRate  = cfg.getTaxRate();            // e.g. 0.08
        double taxAmount = totals.finalTotal * taxRate;
        double grandTotal = totals.finalTotal + taxAmount;

        StringBuilder sb = new StringBuilder();
        sb.append("========= RECEIPT (SRP) =========\n");
        for (OrderLine l : order.getLines()) {
            String name = l.getItem().getName();
            double price = l.getItem().getPrice();
            int   qty   = l.getQuantity();
            double line = l.lineTotal();
            sb.append(String.format("%-16s x%-3d @ %s%5.2f  = %s%6.2f%n",
                    name, qty, currency, price, currency, line));
        }
        sb.append("---------------------------------\n");
        sb.append(String.format("SUBTOTAL:                %s%6.2f%n", currency, totals.subtotal));

        // Discount line
        String label = "DISCOUNT";
        if (totals.discountLabel != null && !totals.discountLabel.isBlank()) {
            label += " - " + totals.discountLabel;
        }
        if (totals.discountPercent != null) {
            label += String.format(" (%.2f%%)", totals.discountPercent);
        }
        sb.append(String.format("%-24s -%s%6.2f%n",
                label + ":", currency, Math.max(0.0, totals.discountAmount)));

        // Total after discount, before tax
        sb.append(String.format("TOTAL (Before Tax):  %s%6.2f%n", currency, totals.finalTotal));

        // Tax line (from Singleton)
        sb.append(String.format("TAX (%.1f%%):            %s%6.2f%n", taxRate * 100, currency, taxAmount));

        // Grand total (after discount + tax)
        sb.append(String.format("GRAND TOTAL:          %s%6.2f%n", currency, grandTotal));

        sb.append("=================================\n");
        return sb.toString();
    }
}