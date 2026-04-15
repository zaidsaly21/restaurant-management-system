package solid.isp;

public interface PaymentCharge {
    // Contract: amount >= 0; implementations should not throw for valid inputs.
    PaymentResult charge(double amount);
}