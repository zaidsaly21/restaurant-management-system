package oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Order {
    private final List<OrderLine> lines = new ArrayList<>();

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public void addItem(MenuItem item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item is required");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be >= 1");
        for (OrderLine l : lines) {
            if (l.getItem().getName().equalsIgnoreCase(item.getName())) {
                l.addQuantity(quantity);
                return;
            }
        }
        lines.add(new OrderLine(item, quantity));
    }

    public boolean removeByName(String itemName) {
        if (itemName == null) return false;
        Iterator<OrderLine> it = lines.iterator();
        while (it.hasNext()) {
            OrderLine l = it.next();
            if (l.getItem().getName().equalsIgnoreCase(itemName.trim())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public double total() {
        double sum = 0.0;
        for (OrderLine l : lines) sum += l.lineTotal();
        return sum;
    }

    public void clear() {
        lines.clear();
    }
}