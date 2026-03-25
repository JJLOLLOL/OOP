package models.character.finances;

public class CharacterFinances {
    private double money;
    
    public CharacterFinances() {
        this.money = 1000.0;
    }
    
    public double getMoney() {
        return money;
    }
    
    public boolean canAfford(double amount) {
        return money >= amount;
    }
    
    public boolean spendMoney(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        if (!canAfford(amount)) {
            return false;
        }
        money -= amount;
        return true;
    }
    
    public void earnMoney(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        money += amount;
    }
}