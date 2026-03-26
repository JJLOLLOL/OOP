# CLI Sims Simulation Game

## Overview

This project is a Java command-line life simulation game inspired by *The Sims*.
The player creates one or more Sims, selects an active Sim, and manages daily life through a menu-driven CLI.

The game currently supports:

- Sim creation and active-character selection
- Real-time in-game clock progression
- Need decay and recovery
- Skills and XP progression
- Careers, shifts, salary, and promotion progress
- Social interactions and relationship changes
- Location changes and NPC schedules
- Furniture-based actions
- House and furniture shopping
- Debuffs triggered by poor need management
- Achievement and notification systems
- Basic JUnit test coverage for selected core/model classes

---

## Gameplay Summary

### Main gameplay loop

After creating Sims, the player enters the gameplay screen and controls one active Sim at a time.
The main gameplay menu allows the player to:

1. Interact with objects at the current location
2. Socialise with nearby characters
3. Change location
4. Switch active Sim
5. Open the shop
6. Exit the game

### Sim systems

Each Sim has the following major systems:

- **Needs**: Hunger, Hygiene, Energy, Fun, Social
- **Skills**: Tracked through XP and level progression
- **Career**: Job selection, salary, rank progression, shift timing
- **Finances**: Starting money, spending, and earning
- **Housing**: Current house, furniture ownership, upgrades
- **Relationships**: Shared relationship scores with other Sims and NPCs

### Locations

The game world includes several locations such as:

- Home
- Restaurant
- Gym
- Park
- Cafe
- Library
- Club
- Office

NPCs move between locations based on daily schedules.

### Furniture and actions

Locations contain furniture, and furniture exposes actions.
Examples include actions such as eating, sleeping, showering, studying, working, exercising, and leisure interactions.
Each action can affect:

- needs
- skill XP
- money
- time taken

### Shop system

The shop currently supports:

- browsing houses
- browsing furniture
- buying houses
- buying furniture
- selling owned furniture

### Achievements and notifications

The game tracks milestone-style achievements, such as career and skill firsts, and displays recent notifications in the right-side panel of the UI.

---

## User Interface

The project uses a text-based CLI interface.

### Create Sim phase

At startup, the player is guided through a multi-step creation flow:

- choose number of Sims
- enter each Sim's name
- enter age
- enter gender
- confirm Sims
- choose the active Sim if more than one Sim was created

### Gameplay phase

During gameplay, the UI renders four side-by-side panels:

- **Stats**: active Sim details, money, needs, current location, nearby characters
- **Actions**: current menu and available options
- **Skills**: skill bars and levels
- **Notifications**: recent events and achievements

The UI is rendered by the `ui` package and displayed with ANSI-coloured console output.

---

## Architecture Overview

The codebase is structured into separate layers for game flow, domain logic, services, and rendering.

### `core`

Owns game startup, game loop, input handling, state, and top-level controllers.

Key classes:

- `Main` — application entry point
- `GameEngine` — main loop, tick cycle, input routing
- `GameState` — shared mutable runtime state
- `GameClock` — in-game time progression
- `CreateSimController` — create-sim flow controller
- `PlayController` — gameplay menu flow controller
- `WorldRegistry` — central access point for world data

### `models`

Owns the domain model and gameplay behaviour.

Main areas:

- `character` — `Character`, `SimCharacter`, `NPCCharacter`, `Relationship`
- `character.stats` — needs and skills owned by the Sim
- `character.finances` — money handling
- `character.housing` — house and furniture ownership logic
- `character.relationship` — relationship map and shared relationship state
- `need` — need classes and need types
- `skill` — skill objects and skill types
- `career` — career definitions and progression
- `actions` — furniture, actions, shop inventory
- `location` — `Location` and `House`
- `debuffs` — debuff rules applied to needs and skills
- `progression` — XP tracker support

### `services`

Owns shared coordination logic that does not belong directly in rendering.

- `NpcService` — updates NPC location by schedule
- `RelationshipService` — relationship registration and social interactions
- `AchievementService` — achievement evaluation and unlocking
- `NotificationService` — temporary gameplay notifications

### `ui`

Owns all CLI rendering.

- `Renderer` — top-level phase-based renderer
- `views` — create-sim view and gameplay view
- `panels` — stats, actions, skills, notifications panels
- `ConsoleUtils` — console formatting helpers

### `test`

Contains JUnit tests for selected classes.
Current test classes include:

- `GameClockTest`
- `RelationshipTest`
- `LocationTest`
- `HouseTest`
- `XpTrackerTest`

---

## Design Notes

### Object-oriented design used in the project

This project applies the following OO ideas in the current implementation:

- **Encapsulation**: state is managed inside domain objects such as `CharacterStats`, `CharacterFinances`, and `CharacterHousing`
- **Inheritance**: `SimCharacter` and `NPCCharacter` extend `Character`
- **Polymorphism**: need behaviour is specialised through concrete `Need` subclasses
- **Composition**: `SimCharacter` composes stats, finances, housing, career, and relationships rather than storing all logic in one place
- **Separation of concerns**: `core`, `models`, `services`, and `ui` are kept distinct
- **Rich domain model**: business rules are placed inside domain classes rather than only in controllers

### Content/data note

Static world content such as locations, furniture definitions, and NPC schedules is currently centralised behind classes such as `WorldRegistry` and `FurnitureFactory`.

In this branch, that content is still assembled in code.
If your team moves that content into text files later, this architecture still holds: the registry/factory layer becomes the loading boundary, while the rest of the gameplay flow can remain unchanged.

---

## Project Structure

```text
OOP/
├─ README.md
├─ src/
│  ├─ Types/
│  ├─ core/
│  ├─ models/
│  │  ├─ actions/
│  │  ├─ career/
│  │  ├─ character/
│  │  │  ├─ finances/
│  │  │  ├─ housing/
│  │  │  ├─ relationship/
│  │  │  └─ stats/
│  │  ├─ debuffs/
│  │  ├─ location/
│  │  ├─ need/
│  │  ├─ progression/
│  │  └─ skill/
│  ├─ services/
│  └─ ui/
│     ├─ panels/
│     └─ views/
└─ test/
   ├─ core/
   └─ models/
```

---

## Requirements

### Required software

- **Java JDK 21 or newer**
- A terminal that supports ANSI output
  - macOS Terminal or iTerm2 recommended
  - Windows Terminal or PowerShell recommended

### Notes

- This project does **not** use Maven or Gradle.
- Source files are compiled manually with `javac`.
- Main entry point: `core.Main`
- Tests use **JUnit 5**.

---

## How to Run the Game

## macOS / Linux

Open Terminal in the project root folder, then run:

```bash
rm -rf out
mkdir -p out/main
find src -name "*.java" > sources.txt
javac -d out/main @sources.txt
java -cp out/main core.Main
```

### What this does

- compiles all source files from `src/`
- places compiled `.class` files into `out/main`
- launches the game through `core.Main`

---

## Windows (PowerShell)

Open PowerShell in the project root folder, then run:

```powershell
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force out\main | Out-Null
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -d out\main @sources.txt
java -cp out\main core.Main
```

### Notes for Windows

- Run this in **PowerShell** or **Windows Terminal**.
- If `javac` or `java` is not recognised, install JDK 21 and make sure it is added to `PATH`.

---

## How to Run Tests

The project uses JUnit 5, but the repository does not include a build tool.
You need a local copy of the **JUnit Platform Console Standalone** jar.

Place the jar in a folder named `lib/` at the project root, for example:

```text
lib/
└─ junit-platform-console-standalone.jar
```

---

## Run Tests on macOS / Linux

```bash
rm -rf out
mkdir -p out/main out/test
find src -name "*.java" > sources.txt
find test -name "*.java" > tests.txt
javac -d out/main @sources.txt
javac -cp "out/main:lib/junit-platform-console-standalone.jar" -d out/test @tests.txt
java -jar lib/junit-platform-console-standalone.jar --class-path "out/main:out/test" --scan-class-path
```

---

## Run Tests on Windows (PowerShell)

```powershell
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force out\main | Out-Null
New-Item -ItemType Directory -Force out\test | Out-Null
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } | Set-Content sources.txt
Get-ChildItem -Recurse -Filter *.java test | ForEach-Object { $_.FullName } | Set-Content tests.txt
javac -d out\main @sources.txt
javac -cp "out/main;lib/junit-platform-console-standalone.jar" -d out\test @tests.txt
java -jar lib/junit-platform-console-standalone.jar --class-path "out/main;out/test" --scan-class-path
```

---

## Optional: Running in VS Code

If you are using VS Code on either macOS or Windows:

1. Open the project folder.
2. Make sure JDK 21 is selected.
3. Install the Java extensions needed for running Java and JUnit tests.
4. Run `src/core/Main.java`.
5. Use the Testing panel to run JUnit tests.

This is optional. The command-line steps above are the manual setup the project expects.

---

## Basic Controls / How to Play

- Enter numbers to select menu options.
- Follow on-screen prompts during the create-sim phase.
- Use `0` where shown to go back from sub-menus.
- During gameplay:
  - interact with furniture to perform actions
  - socialise to change relationships
  - change location to access different characters and objects
  - work to earn money once a career is chosen
  - buy furniture or houses through the shop
  - switch active Sim if multiple Sims exist

---

## Known Implementation Notes

- The current project is CLI-only.
- Some static session state still exists inside controllers/services.
- Static world content is currently code-backed, though it can be moved to text-file loaders behind the same content boundary.
- Test coverage exists, but it does not yet cover every gameplay class.

---

## Authors / Team Contribution

Update this section to match your team.
Example:

- Developer 1 — game engine / controllers / CLI rendering
- Developer 2 — world content / data loading / NPC setup
- Developer 3 — domain systems / careers / housing / actions
- Developer 4 — testing / QA / documentation

---
