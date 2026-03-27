package types;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InteractionTypeTest {

    @Test
    void values_containsAllInteractionsInOrder() {
        InteractionType[] interactions = InteractionType.values();

        assertEquals(4, interactions.length);
        assertEquals(InteractionType.TALK, interactions[0]);
        assertEquals(InteractionType.COMPLIMENT, interactions[1]);
        assertEquals(InteractionType.ARGUE, interactions[2]);
        assertEquals(InteractionType.INSULT, interactions[3]);
    }

    @Test
    void getters_returnCorrectValues() {
        assertEquals("Talk", InteractionType.TALK.getLabel());
        assertEquals(5, InteractionType.TALK.getEffect());
        assertEquals(" responds positively to the conversation.",
                InteractionType.TALK.getReaction());

        assertEquals("Compliment", InteractionType.COMPLIMENT.getLabel());
        assertEquals(10, InteractionType.COMPLIMENT.getEffect());

        assertEquals("Argue", InteractionType.ARGUE.getLabel());
        assertEquals(-10, InteractionType.ARGUE.getEffect());

        assertEquals("Insult", InteractionType.INSULT.getLabel());
        assertEquals(-15, InteractionType.INSULT.getEffect());
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(InteractionType.TALK, InteractionType.valueOf("TALK"));
        assertEquals(InteractionType.COMPLIMENT, InteractionType.valueOf("COMPLIMENT"));
        assertEquals(InteractionType.ARGUE, InteractionType.valueOf("ARGUE"));
        assertEquals(InteractionType.INSULT, InteractionType.valueOf("INSULT"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> InteractionType.valueOf("INVALID"));
    }
}