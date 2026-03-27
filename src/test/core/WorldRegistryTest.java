package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import models.character.NPCCharacter;
import models.location.Location;
import ui.UITestSupport;

class WorldRegistryTest {

    @Test
    void gettersExposeConfiguredLocationsAndNpcs() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        assertSame(fixture.home, fixture.world.getLocation("Home"));
        assertSame(fixture.park, fixture.world.getLocation("Park"));
        assertSame(fixture.npc, fixture.world.getAllNPCs().get(0));
        assertEquals(3, fixture.world.getAllLocations().size());
    }

    @Test
    void returnedCollectionsAreUnmodifiable() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        Collection<Location> locations = fixture.world.getAllLocations();
        List<NPCCharacter> npcs = fixture.world.getAllNPCs();

        assertThrows(UnsupportedOperationException.class, () -> locations.add(fixture.cafe));
        assertThrows(UnsupportedOperationException.class, () -> npcs.add(fixture.npc));
    }
}
