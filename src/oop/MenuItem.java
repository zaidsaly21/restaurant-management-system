package oop;

import java.util.Objects;

public class MenuItem {
    private final String name;
    private final String description;
    private final double price;

    public MenuItem(String name, String description, double price) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is required");
        if (price < 0) throw new IllegalArgumentException("price must be >= 0");
        this.name = name.trim();
        this.description = description == null ? "" : description.trim();
        this.price = price;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("%-16s $%5.2f  - %s", name, price, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem that = (MenuItem) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}