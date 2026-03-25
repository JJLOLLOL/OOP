package models.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import models.actions.Furniture;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void constructorCreatesEmptyFurnitureListWhenInputIsNull() {
        Location location = new Location("Park", null);

        assertEquals("Park", location.getLocationName());
        assertEquals(0, location.getFurnitureViews().size());
    }

    @Test
    void furnitureViewIsUnmodifiable() {
        ArrayList<Furniture> furnitures = new ArrayList<>();
        furnitures.add(new Furniture("Sofa", "Comfortable seat", 200));
        Location location = new Location("Living Room", furnitures);

        List<Furniture> furnitureView = location.getFurnitureViews();

        assertEquals(1, furnitureView.size());
        assertThrows(UnsupportedOperationException.class,
                () -> furnitureView.add(new Furniture("TV", "Large screen", 500)));
    }

    @Test
    void npcViewStartsEmpty() {
        Location location = new Location("Kitchen", new ArrayList<>());

        assertEquals(0, location.getNpcViews().size());
    }
}
