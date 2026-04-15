package solid.ocp;

public interface DiscountPolicy {
    // Returns the final total after applying the discount to the given subtotal
    double apply(double subtotal);
}