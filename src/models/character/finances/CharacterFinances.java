package models.character.finances;

/**
 * Encapsulates a sim's money balance and basic money operations.
 */
public class CharacterFinances {
    private double money;

    /**
     * Creates a new balance with the default starting funds.
     */
    public CharacterFinances() {
        this.money = 1000.0;
    }

    public double getMoney() {
        return money;
    }

    /**
     * Returns whether the current balance can cover the supplied amount.
     *
     * @param amount the amount to check
     * @return {@code true} when enough money is available
     */
    public boolean canAfford(double amount) {
        return money >= amount;
    }

    /**
     * Deducts money when sufficient funds are available.
     *
     * @param amount the amount to spend
     * @return {@code true} when the deduction succeeded
     */
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

    /**
     * Adds money to the current balance.
     *
     * @param amount the amount to add
     */
    public void earnMoney(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        money += amount;
    }
}
