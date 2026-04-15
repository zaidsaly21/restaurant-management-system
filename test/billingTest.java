

import org.junit.Test;
import static org.junit.Assert.*;
import designPatterns.strategy.PricingStrategies;
import solid.ocp.NoDiscountPolicy;
import solid.ocp.PercentageDiscountPolicy;

public class billingTest {

    @Test
    public void testNoDiscount() {
        double subtotal = 80.0;
        PricingStrategies.Selection selection =
                PricingStrategies.apply(new solid.ocp.NoDiscountPolicy(), subtotal);

        double finalTotal = selection.finalTotal;

        assertEquals(subtotal, finalTotal, 0.01);
        assertEquals("None", selection.label);
        assertNull(selection.percent);
        assertEquals(0.0, selection.discountAmount, 0.01);
    }

    @Test
    public void testPercentageDiscount() {
        double subtotal = 150.0;
        solid.ocp.PercentageDiscountPolicy policy = new solid.ocp.PercentageDiscountPolicy(20.0);
        PricingStrategies.Selection selection = PricingStrategies.apply(policy, subtotal);

        double finalTotal = selection.finalTotal;

        assertEquals(120.0, finalTotal, 0.01); // 20% discount
        assertEquals("Percentage", selection.label);
        assertEquals(Double.valueOf(20.0), selection.percent);
        assertEquals(30.0, selection.discountAmount, 0.01);
    }

    @Test
    public void testApplyPercentageWithZeroSubtotal() {
        double subtotal = 0.0;
        solid.ocp.PercentageDiscountPolicy policy = new solid.ocp.PercentageDiscountPolicy(10.0);
        PricingStrategies.Selection selection = PricingStrategies.apply(policy, subtotal);

        double finalTotal = selection.finalTotal;

        assertEquals(0.0, finalTotal, 0.01);
        assertEquals("Percentage", selection.label);
        assertEquals(Double.valueOf(10.0), selection.percent);
        assertEquals(0.0, selection.discountAmount, 0.01);
    }

    @Test
    public void testNegativeDiscountHandled() {
        double subtotal = 50.0;
       
        solid.ocp.PercentageDiscountPolicy policy = new solid.ocp.PercentageDiscountPolicy(120.0);
        PricingStrategies.Selection selection = PricingStrategies.apply(policy, subtotal);

        double finalTotal = selection.finalTotal;

        assertEquals(-10.0, finalTotal, 0.01); // subtotal - discount
        assertEquals("Percentage", selection.label);
        assertEquals(Double.valueOf(120.0), selection.percent);
        assertEquals(60.0, selection.discountAmount, 0.01);
    }
}
