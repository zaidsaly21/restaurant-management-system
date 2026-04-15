package solid.srp;

public class ReceiptTotals {
    public final double subtotal;
    public final String discountLabel;      
    public final Double discountPercent;   
    public final double discountAmount;     
    public final double finalTotal;         

    public ReceiptTotals(double subtotal, String discountLabel, Double discountPercent, double discountAmount, double finalTotal) {
        this.subtotal = subtotal;
        this.discountLabel = discountLabel == null ? "" : discountLabel;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalTotal = finalTotal;
    }
}