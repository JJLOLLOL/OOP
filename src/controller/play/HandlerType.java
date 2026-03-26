package controller.play;

/**
 * An enumeration of all possible handlers that can be active during the PLAYING phase.
 * Used by the PlayController to switch between different game feature contexts.
 */
public enum HandlerType {
    MAIN_MENU,
    INTERACTION,
    SOCIAL,
    LOCATION_CHANGE,
    SWITCH_CHARACTER,
    PICK_CAREER,
    SHOP
}