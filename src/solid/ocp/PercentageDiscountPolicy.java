package solid.ocp;

public class PercentageDiscountPolicy implements DiscountPolicy {
    private final double percent; // e.g., 10 means 10%

    public PercentageDiscountPolicy(double percent) {
        if (percent < 0) throw new IllegalArgumentException("percent must be >= 0");
        this.percent = percent;
    }

    @Override
    public double apply(double subtotal) {
        return subtotal * (1.0 - percent / 100.0);
    }

    public double getPercent() {
        return percent;
    }
}