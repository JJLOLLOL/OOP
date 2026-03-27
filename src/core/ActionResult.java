package core;

/**
 * Immutable success or failure result returned by gameplay actions together
 * with a player-facing message.
 */
public class ActionResult {
    private final boolean success;
    private final String message;

    private ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Creates a successful action result.
     *
     * @param message the message to show to the player
     * @return a success result
     */
    public static ActionResult success(String message) {
        return new ActionResult(true, message);
    }

    /**
     * Creates a failed action result.
     *
     * @param message the message to show to the player
     * @return a failure result
     */
    public static ActionResult failure(String message) {
        return new ActionResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
