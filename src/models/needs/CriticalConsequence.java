package models.needs;

/**
 * Immutable data class describing the consequences when a need becomes
 * critically low.
 */
public class CriticalConsequence {

    private final String notificationMessage;
    private final AffectedNeed[] affectedNeeds; // Needs to modify as consequence
    private final AffectedSkill[] affectedSkills; // Skills to modify as consequence

    // Overloaded constructor for needs and skills
    public CriticalConsequence(String notificationMessage,
            AffectedNeed[] affectedNeeds,
            AffectedSkill[] affectedSkills) {
        this.notificationMessage = notificationMessage;
        this.affectedNeeds = affectedNeeds != null ? affectedNeeds : new AffectedNeed[0];
        this.affectedSkills = affectedSkills != null ? affectedSkills : new AffectedSkill[0];
    }

    // Overloaded constructor for needs only
    public CriticalConsequence(String notificationMessage, AffectedNeed... affectedNeeds) {
        this(notificationMessage, affectedNeeds, null);
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public AffectedNeed[] getAffectedNeeds() {
        return affectedNeeds;
    }

    public AffectedSkill[] getAffectedSkills() {
        return affectedSkills;
    }

    /**
     * Immutable data for a need affected by this consequence.
     */
    public static class AffectedNeed {

        private final String needName;
        private final double adjustment;

        public AffectedNeed(String needName, double adjustment) {
            this.needName = needName;
            this.adjustment = adjustment;
        }

        public String getNeedName() {
            return needName;
        }

        public double getAdjustment() {
            return adjustment;
        }
    }

    /**
     * Immutable data for a skill affected by this consequence.
     */
    public static class AffectedSkill {

        private final String skillName;
        private final double adjustment;

        public AffectedSkill(String skillName, double adjustment) {
            this.skillName = skillName;
            this.adjustment = adjustment;
        }

        public String getSkillName() {
            return skillName;
        }

        public double getAdjustment() {
            return adjustment;
        }
    }
}
