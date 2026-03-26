package models.need;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SocialTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Social social = new Social();

        assertEquals(NeedType.SOCIAL, social.getType());
        assertEquals("Social", social.getNeedName());
        assertEquals(80.0, social.getValue());
        assertEquals(3.0, social.getDecayRate());
        assertEquals(3.0, social.getBaseDecayRate());
    }
}