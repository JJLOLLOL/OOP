package models.character.stats;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;

import models.need.Energy;
import models.need.Fun;
import models.need.Hunger;
import models.need.Hygiene;
import models.need.Need;
import models.need.NeedType;
import models.need.Social;
import models.skill.Skill;
import models.skill.SkillType;

public class CharacterStats {
    private final EnumMap<NeedType, Need> needs;
    private final EnumMap<SkillType, Skill> skills;
    
    public CharacterStats() {
        this.needs = createDefaultNeeds();
        this.skills = createDefaultSkills();
    }
    
    private EnumMap<NeedType, Need> createDefaultNeeds() {
        EnumMap<NeedType, Need> map = new EnumMap<>(NeedType.class);
        map.put(NeedType.HUNGER, new Hunger());
        map.put(NeedType.HYGIENE, new Hygiene());
        map.put(NeedType.ENERGY, new Energy());
        map.put(NeedType.FUN, new Fun());
        map.put(NeedType.SOCIAL, new Social());
        return map;
    }

    private EnumMap<SkillType, Skill> createDefaultSkills() {
        EnumMap<SkillType, Skill> map = new EnumMap<>(SkillType.class);
        for (SkillType type : SkillType.values()) {
            map.put(type, new Skill(type));
        }
        return map;
    }


    // skills
    // getter
    public Skill getSkill(SkillType type) {
        if (type == null) {
            throw new IllegalArgumentException("Skill type cannot be null.");
        }
        return skills.get(type);
    }
    public int getSkillLevel(SkillType type) {
        return getSkill(type).getLevel();
    }

    public double getSkillXp(SkillType type) {
        return getSkill(type).getProgress();
    }
    public Collection<Skill> getSkillViews() {
        return Collections.unmodifiableCollection(skills.values());
    }

    // setter
    public int adjustSkillXpRaw(SkillType type, double xp) {
        if (type == null) {
            throw new IllegalArgumentException("Skill type cannot be null.");
        }

        Skill skill = skills.get(type);
        if (skill == null) {
            throw new IllegalStateException("Skill not found: " + type);
        }
        return skill.addProgress(xp);
    }


    // needs
    // getter
    public Need getNeed(NeedType type) {
        if (type == null) {
            throw new IllegalArgumentException("Need type cannot be null.");
        }

        Need need = needs.get(type);
        if (need == null) {
            throw new IllegalStateException("Need not found: " + type);
        }

        return need;
    }
    public Collection<Need> getNeedViews() {
        return Collections.unmodifiableCollection(needs.values());
    }
    
    // setter
    public void adjustNeedRaw(NeedType type, double amount) {
        if (type == null) {
            throw new IllegalArgumentException("Need type cannot be null.");
        }
        Need need = needs.get(type);
        if (need == null) {
            throw new IllegalStateException("Need not found: " + type);
        }
        need.adjustValue(amount);
    }
}
