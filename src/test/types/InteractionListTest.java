package types;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import testTypes.InteractionList;

class InteractionListTest {

    @Test
    void values_containsAllInteractionsInOrder() {
        InteractionList[] interactions = InteractionList.values();

        assertEquals(4, interactions.length);
        assertEquals(InteractionList.TALK, interactions[0]);
        assertEquals(InteractionList.COMPLIMENT, interactions[1]);
        assertEquals(InteractionList.ARGUE, interactions[2]);
        assertEquals(InteractionList.INSULT, interactions[3]);
    }

    @Test
    void getters_returnCorrectValues() {
        assertEquals("Talk", InteractionList.TALK.getLabel());
        assertEquals(5, InteractionList.TALK.getEffect());
        assertEquals(" responds positively to the conversation.",
                InteractionList.TALK.getReaction());

        assertEquals("Compliment", InteractionList.COMPLIMENT.getLabel());
        assertEquals(10, InteractionList.COMPLIMENT.getEffect());

        assertEquals("Argue", InteractionList.ARGUE.getLabel());
        assertEquals(-10, InteractionList.ARGUE.getEffect());

        assertEquals("Insult", InteractionList.INSULT.getLabel());
        assertEquals(-15, InteractionList.INSULT.getEffect());
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(InteractionList.TALK, InteractionList.valueOf("TALK"));
        assertEquals(InteractionList.COMPLIMENT, InteractionList.valueOf("COMPLIMENT"));
        assertEquals(InteractionList.ARGUE, InteractionList.valueOf("ARGUE"));
        assertEquals(InteractionList.INSULT, InteractionList.valueOf("INSULT"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> InteractionList.valueOf("INVALID"));
    }
}