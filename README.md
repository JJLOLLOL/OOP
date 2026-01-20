# Conceptual Game Design & System Architecture

## Project Overview
**Focus:** Conceptual game design and system architecture.
**Status:** No full working game is required at this stage. The emphasis is on design clarity, feasibility, and object-oriented thinking.

> **Concept:** A "The Sims" simulation game played in a Command Line Interface (CLI).

---

## 1. Gameplay Mechanics

### Core Features
* **Simulation Style:** Similar to *The Sims* or *BitLife*.
* **Time Flow:**
    * Time passes continuously.
    * Ratio: **1 minute (Real World) = 1 hour (Sim World)**.
    * Activities are locked/unlocked based on in-game time.
* **Activities:**
    * Player performs actions (e.g., "Eat Lunch") which directly affect specific main attributes.
    * NPCs appear based on the activities or locations selected.

### Character Bio (Static Data)
* Name
* Age (Fixed value)
* Gender
* Occupation

### Main Attributes (Dynamic Needs)
*Note: These attributes undergo continuous change over time regardless of player action.*

| Attribute | Associated Activity | Effect/Notes |
| :--- | :--- | :--- |
| **Hunger** | Eat | Might cost money. |
| **Social** | Talk to NPC | Affects relationship status. |
| **Bladder** | Toilet Break | Restores bladder need. |
| **Energy** | Sleep | Restores energy. |
| **Hygiene** | Shower | Restores hygiene. |
| **Fun** | Watch TV | Increases fun. |
| **Money** | Work | Increases balance, consumes time/energy. |
| **Mood** | *Derived* | Calculated based on overall status. |

---

## 2. System Architecture

### Key Classes
The system relies on a hierarchy of classes to manage game entities.

#### Character Hierarchy
* **`Character` (Parent Class):** Base class for all entities.
* **`SimCharacter` (Child Class):** The player-controlled character (Singleton/Game only has 1).
* **`NPCCharacter` (Child Class):** Non-player characters (Game has >1).

#### Support Classes
* **`Activities`:** Defines available actions and their costs/rewards.
* **`Need`:** Manages the logic for hunger, energy, etc.
* **`Progression`:** Handles leveling or game advancement.
* **`Career`:** Manages job progression, salary, and work duration.
* **`Skills`:** Tracks player abilities.
* **`Location`:** Defines where the character is (affects available NPCs).

### Relationships
Relationships are tracked between any two characters using a numerical value (-100 to 100).

* **Enemies:** -100 to -25
* **Strangers:** -25 to 0
* **Acquaintance/Friends:** 0 to 100

### Object-Oriented Concepts (Planned)
The design focuses on demonstrating:
* **Encapsulation:** Protecting character attributes.
* **Inheritance:** Shared logic between `SimCharacter` and `NPCCharacter`.
* **Polymorphism:** Different behaviors for the same methods (e.g., how a generic `Character` vs. a `Sim` interacts).

---

## 3. Team Roles & Development

* **GUI & Gameplay:** 1 Developer
* **Game Logic:** 2 Developers
* **Test Code:** 1 Developer

---

## 4. Submission Deliverables

**Requirement:** Submit a video recording presenting the following:

1. **Overall Game Concept:** Bit-life simulation in CLI.
2. **Core Gameplay Mechanics:** Needs, activities, and time flow.
3. **Proposed System Architecture:** Diagram of key classes and hierarchy.
4. **Planned OO Concepts:** Explanation of polymorphism, encapsulation, and inheritance usage.
5. **High-Level CLI Interaction:**
    * Mocked flow or narrated walkthrough.
    * *Note: Use screenshots previously shared in Telegram/Messaging.*