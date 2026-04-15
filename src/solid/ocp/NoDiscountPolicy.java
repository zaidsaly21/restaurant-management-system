package solid.ocp;

public class NoDiscountPolicy implements DiscountPolicy {
    @Override
    public double apply(double subtotal) {
        return subtotal;
    }
}