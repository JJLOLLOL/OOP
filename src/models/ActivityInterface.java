package models;

public interface ActivityInterface {

    void execute(Character character);
    public void affectedNeeds();
    public void affectedSkills();
    public void moneyDeducted();
}
