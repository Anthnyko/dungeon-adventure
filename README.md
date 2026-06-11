# 🏰 Dungeon Adventure

**Class:** TCSS 360 — Software Development and Quality Assurance  
**Quarter:** Spring 2026

## 👥 Team Members
- **Anthony Co**
- **Ibrahim Mohamud**
- **Jackson Steger**

---

# 🎮 Project Overview
**Dungeon Adventure** is a turn‑based, object‑oriented RPG built in Java.  
Players explore a procedurally generated dungeon, battle monsters, collect items, and attempt to recover all four **Pillars of Object‑Oriented Programming** to escape.

The project emphasizes:

- 🧱 Strong class hierarchy & inheritance
- ⚔️ Polymorphic combat behavior
- 🧪 Testable, modular design
- 🧭 Team‑based development and Agile workflow
- 🗂️ Issue tracking and sprint planning using YouTrack

Our team used YouTrack to manage tasks, track progress, assign responsibilities, and maintain a clear development workflow throughout each iteration.

---

# ✨ Major Features

## Core Gameplay
- Procedurally generated dungeon layout
- Turn‑based combat system
- Hero classes with unique abilities
- SQLite‑backed monster database
- Items, potions, and room events
- Win condition: collect all four OO Pillars and reach the exit

## Extended Features (Beyond Requirements)
- **Fog of War** exploration system
- **Ultimate abilities** and **status effects** (bleed, stun, heal, etc.)
- **Difficulty levels** (affects monster stats and encounter rates)
- **New room events:** Alarm, Fountain, Poison
- **Monster pathfinding** triggered by the Alarm event
- **Vision potion** that reveals surrounding rooms
- **Color‑formatted UI** for readability

---

# ▶️ How to Run the Project

## Prerequisites
- Java 17+
- SQLite JDBC driver (included in `/lib`)
- Terminal or IDE (IntelliJ recommended)

## Running from IntelliJ
1. Clone the repository
2. Open the project in IntelliJ
3. Ensure the SQLite JDBC JAR is added to the project classpath
4. Run the `DungeonAdventure` main class
5. Play in the terminal window

## Running from Command Line
From the project root:

```bash
javac -cp "lib/sqlite-jdbc.jar" -d out $(find src -name "*.java")
java -cp "out:lib/sqlite-jdbc.jar" DungeonAdventure
```
**Note:** On Windows, replace : with ; in the classpath.

---

# 📁 Repository Structure

```
DungeonAdventure/
│
├── src/
│   ├── main/
│   │   ├── dungeon/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   │   ├── characters/
│   │   │   │   ├── Dungeon/
│   │   │   │   ├── items/
│   │   │   │   ├── RoomEvent/
│   │   │   │   └── Saving/
│   │   │   ├── util/
│   │   │   └── view/
│   │
│   └── test/
│       └── dungeon.model/
│           ├── characters/
│           ├── dungeon/
│           ├── events/
│           └── Saves/
│
├── lib/
│   └── sqlite-jdbc.jar
│
├── dungeon_saves.db
│
├── MonsterDatabase.db
│
└── README.md

```

---

# 🧠 Design & Architecture
- MVC Architecture
- Factory Pattern for monster creation
- Inheritance & Polymorphism for hero/monster hierarchies
- Composition for room contents
- Encapsulation of combat logic and room behavior
- Data‑Driven Design using SQLite

---

# 🚀 Future Improvements
- Additional hero classes
- More room events (traps, puzzles, environmental hazards)
- Full graphical UI
- Expanded save/load system
- Larger dungeon sizes
- Sound effects and music

---

# 🙌 Contributors

- **Anthony Co** — BattleController (Combat), Hero/Monster Classes, Hero Abilities, Cooldowns, Monster Behavior
- **Ibrahim Mohamud** — Dungeon (Model), Room (Model), RoomEvent (Model), MonsterGenerator, Monster Database Integration
- **Jackson Steger** — User interface, DungeonView, DungeonAdventure (Controller), Saving and Loading