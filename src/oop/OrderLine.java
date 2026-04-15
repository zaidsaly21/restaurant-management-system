package oop;

public class OrderLine {
    private final MenuItem item;
    private int quantity;

    public OrderLine(MenuItem item, int quantity) {
        if (item == null) throw new IllegalArgumentException("item is required");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be >= 1");
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() { return item; }
    public int getQuantity() { return quantity; }

    public void addQuantity(int delta) {
        if (delta <= 0) throw new IllegalArgumentException("delta must be >= 1");
        this.quantity += delta;
    }

    public double lineTotal() {
        return item.getPrice() * quantity;
    }
}