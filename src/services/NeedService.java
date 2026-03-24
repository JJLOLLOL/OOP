package services;

import models.SimCharacter;
import models.Skills;
import models.debuffs.DebuffRegistry;
import models.needs.Need;

/**
 * Single point of entry for mutating a {@link SimCharacter}'s needs and skills.
 *
 * <p>
 * All debuff modifiers are applied here before the raw value reaches the
 * character, so {@link SimCharacter} itself has no knowledge of
 * {@link DebuffRegistry}.
 */
public class NeedService {

    private NeedService() {
    }

    /**
     * Adjusts {@code needName} by {@code amount} after running debuff
     * modifiers.
     *
     * @param sim the {@link SimCharacter} whose need is adjusting
     * @param needName the name of the need to adjust
     * @param amount the base amount to adjust by (positive or negative)
     */
    public static void adjustNeed(SimCharacter sim, String needName, double amount) {
        double modified = DebuffRegistry.applyNeedModifiers(sim, needName, amount);
        Need need = sim.getNeeds().get(needName);
        if (need != null) {
            need.adjustNeed(modified);
        }
    }

    /**
     * Adds {@code amount} XP to {@code skillName} after running debuff
     * modifiers.
     *
     * @param sim the {@link SimCharacter} gaining the skill progress
     * @param skillName the name of the skill
     * @param amount the base amount of XP to add
     * @return the level-up message from the skill, or a plain progress string
     */
    public static String addSkillProgress(SimCharacter sim, String skillName, double amount) {
        double modified = DebuffRegistry.applySkillModifiers(sim, skillName, amount);
        Skills skill = sim.getAllSkills().get(skillName);
        if (skill == null) {
            return "Skill " + skillName + " not found!";
        }
        return skill.addProgress(modified);
    }

    /**
     * Decays all needs for one tick, applying debuff-modified decay rates. Also
     * fires critically-low callbacks when a need crosses the threshold.
     *
     * @param sim the sim whose needs to decay
     * @param deltaTime elapsed real seconds for this tick
     */
    public static void updateNeeds(SimCharacter sim, double deltaTime) {
        for (Need need : sim.getNeeds().values()) {

            // Reset decay multiplier at the start of each tick before applying new modifiers
            need.resetDecayModifier();

            // Calculate modified decay to a multiplier
            double modifiedDecay = DebuffRegistry.applyDecayModifiers(
                    sim, need.getNeedName(), need.getBaseDecayRate());

            // Convert modified decay to a multiplier for this tick
            double multiplier = modifiedDecay / need.getBaseDecayRate();
            need.applyDecayModifier(multiplier);

            // Decay with effective (base * multiplier)
            need.decay(deltaTime);

            // Check for critical state and trigger callbacks
            if (need.isCriticallyLow()) {
                if (!need.isCriticallyLowNotified()) {
                    need.onCriticallyLow(sim);
                    need.setCriticallyLowNotified(true);
                }
            } else {
                need.setCriticallyLowNotified(false);
            }
        }
    }
}
