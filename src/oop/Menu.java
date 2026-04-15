package oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private final List<MenuItem> items = new ArrayList<>();

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean addItem(MenuItem item) {
        if (item == null) return false;
        if (findByName(item.getName()) != null) return false; // prevent duplicates by name
        items.add(item);
        return true;
    }

    public boolean removeByName(String name) {
        if (name == null) return false;
        return items.removeIf(i -> i.getName().equalsIgnoreCase(name.trim()));
    }

    public MenuItem findByName(String name) {
        if (name == null) return null;
        String needle = name.trim().toLowerCase();
        for (MenuItem i : items) {
            if (i.getName().toLowerCase().equals(needle)) return i;
        }
        return null;
    }
}