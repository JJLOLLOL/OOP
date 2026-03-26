package types;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class GenderTest {

    @Test
    void values_containsAllGendersInOrder() {
        Gender[] genders = Gender.values();

        assertEquals(2, genders.length);
        assertEquals(Gender.MALE, genders[0]);
        assertEquals(Gender.FEMALE, genders[1]);
    }

    @Test
    void getters_returnCorrectValues() {
        assertEquals("Male", Gender.MALE.getDisplayName());
        assertEquals("M", Gender.MALE.getLabel());

        assertEquals("Female", Gender.FEMALE.getDisplayName());
        assertEquals("F", Gender.FEMALE.getLabel());
    }

    @Test
    void fromUserInput_acceptsShortAndFullForms_caseInsensitive() {
        assertEquals(Gender.MALE, Gender.fromUserInput("M"));
        assertEquals(Gender.MALE, Gender.fromUserInput("male"));
        assertEquals(Gender.MALE, Gender.fromUserInput("  MaLe  "));

        assertEquals(Gender.FEMALE, Gender.fromUserInput("F"));
        assertEquals(Gender.FEMALE, Gender.fromUserInput("female"));
        assertEquals(Gender.FEMALE, Gender.fromUserInput("  FeMaLe  "));
    }

    @Test
    void fromUserInput_throwsExceptionWhenInputIsNull() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> Gender.fromUserInput(null));

        assertEquals("Gender input cannot be null.", ex.getMessage());
    }

    @Test
    void fromUserInput_throwsExceptionWhenInputIsInvalid() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> Gender.fromUserInput("x"));

        assertEquals("Invalid gender input: x", ex.getMessage());
    }

    @Test
    void fromDataValue_acceptsShortAndFullForms_caseInsensitive() {
        assertEquals(Gender.MALE, Gender.fromDataValue("M"));
        assertEquals(Gender.MALE, Gender.fromDataValue("male"));
        assertEquals(Gender.MALE, Gender.fromDataValue("  MALE  "));

        assertEquals(Gender.FEMALE, Gender.fromDataValue("F"));
        assertEquals(Gender.FEMALE, Gender.fromDataValue("female"));
        assertEquals(Gender.FEMALE, Gender.fromDataValue("  Female  "));
    }

    @Test
    void fromDataValue_throwsExceptionWhenInputIsNull() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> Gender.fromDataValue(null));

        assertEquals("Gender data value cannot be null.", ex.getMessage());
    }

    @Test
    void fromDataValue_throwsExceptionWhenInputIsInvalid() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> Gender.fromDataValue("x"));

        assertEquals("Unknown gender value: x", ex.getMessage());
    }
}