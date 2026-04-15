package solid.ocp;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A one-time (single date) happy hour discount policy.
 * Applies the percentage only if:
 *   - Today's date equals the configured date AND
 *   - Current time is within the window [start, end] (inclusive).
 * Supports windows that cross midnight (though that is unusual for single-date events).
 */
public class DatedHappyHourDiscountPolicy implements DiscountPolicy {

    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private final double percent;

    public DatedHappyHourDiscountPolicy(LocalDate date,
                                        LocalTime start,
                                        LocalTime end,
                                        double percent) {
        if (percent < 0) throw new IllegalArgumentException("percent must be >= 0");
        this.date = date;
        this.start = start;
        this.end = end;
        this.percent = percent;
    }

    @Override
    public double apply(double subtotal) {
        if (isActiveNow()) {
            return subtotal * (1.0 - percent / 100.0);
        }
        return subtotal;
    }

    /**
     * Returns true if current date/time is within the configured window.
     */
    public boolean isActiveNow() {
        LocalDate today = LocalDate.now();
        if (!today.equals(date)) return false;

        LocalTime now = LocalTime.now();
        if (start.equals(end)) return true; // treat as full-day
        if (start.isBefore(end)) {
            return !now.isBefore(start) && !now.isAfter(end);
        }
        // crosses midnight
        return !now.isBefore(start) || !now.isAfter(end);
    }

    public double getPercent() {
        return percent;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}