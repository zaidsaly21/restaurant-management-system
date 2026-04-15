

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import oop.MenuItem;
import oop.Order;
public class orderTest {

    private MenuItem burger;
    private MenuItem fries;
    private Order order;

    @Before
    public void setUp() {
        burger = new MenuItem("Burger", "Beef patty with lettuce", 8.50);
        fries = new MenuItem("Fries", "Crispy french fries", 3.00);
        order = new Order();
    }

    @Test
    public void testAddSingleItem() {
        order.addItem(burger, 2);
        assertEquals(1, order.getLines().size());
        assertEquals(17.0, order.total(), 0.01);
    }

    @Test
    public void testAddDuplicateItemIncreasesQuantity() {
        order.addItem(burger, 1);
        order.addItem(burger, 2);

        assertEquals(1, order.getLines().size());
        assertEquals(3, order.getLines().get(0).getQuantity());
        assertEquals(25.50, order.total(), 0.01);
    }

    @Test
    public void testAddMultipleDifferentItems() {
        order.addItem(burger, 1);
        order.addItem(fries, 2);

        assertEquals(2, order.getLines().size());
        assertEquals(14.50, order.total(), 0.01);
    }

    @Test
    public void testRemoveByName() {
        order.addItem(burger, 1);
        order.addItem(fries, 2);

        boolean removed = order.removeByName("fries");

        assertTrue(removed);
        assertEquals(1, order.getLines().size());
        assertEquals(8.50, order.total(), 0.01);
    }

    @Test
    public void testRemoveNonexistentItem() {
        order.addItem(burger, 1);

        boolean removed = order.removeByName("Pizza");

        assertFalse(removed);
        assertEquals(1, order.getLines().size());
    }

    @Test
    public void testClearOrder() {
        order.addItem(burger, 2);
        order.addItem(fries, 1);

        order.clear();

        assertTrue(order.isEmpty());
        assertEquals(0.0, order.total(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemWithInvalidQuantityThrows() {
        order.addItem(burger, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullItemThrows() {
        order.addItem(null, 1);
    }
}
