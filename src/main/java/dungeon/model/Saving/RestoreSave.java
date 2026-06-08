package dungeon.model.Saving;


import dungeon.model.Dungeon;
import dungeon.model.MonsterGenerator;
import dungeon.model.Room;
import dungeon.model.RoomEvent.AlarmEvent;
import dungeon.model.RoomEvent.FountainEvent;
import dungeon.model.RoomEvent.PitEvent;
import dungeon.model.RoomEvent.PoisonEvent;
import dungeon.model.characters.Hero;
import dungeon.model.characters.Monster;
import dungeon.model.characters.Priest;
import dungeon.model.characters.Thief;
import dungeon.model.characters.Warrior;
import dungeon.model.items.HealingPotion;
import dungeon.model.items.VisionPotion;

/**
 * Restores a saved game state by deserializing and reconstructing the dungeon layout,
 * room data, and hero from a {@link GameState} object produced by {@link InstanceCapture}.
 * Handles restoration of all room properties including doors, pillars, monsters, items,
 * revealed state, and events. The dungeon is initialized with blank rooms before
 * restoration to prevent random state pollution from the default Room constructor.
 *
 * @author Jackson Steger
 * @version 1.0
 * @see GameState
 * @see InstanceCapture
 */
public class RestoreSave {


    public class RestoreResult {
        public final Dungeon myDungeon;
        public final Hero myHero;

        public RestoreResult(final Dungeon theDungeon, final Hero theHero) {
            myDungeon = theDungeon;
            myHero = theHero;
        }
    }

    /**
     * Restores the dungeon and room states from a saved GameState.
     * 
     * @param state the GameState to restore from
     */
    public RestoreResult restoreGameState(final GameState state) {
        // Initialize dungeon with saved dimensions
        final Dungeon myDungeon = new Dungeon(state.myDungeonWidth, state.myDungeonHeight, state.myDungeonName, true);
        
        // Recreate hero from saved class
        final Hero myHero = switch (state.myHeroClass) {
            case "Warrior" -> new Warrior(state.myHeroName, state.myHealingPotions, state.myVisionPotions);
            case "Priest" -> new Priest(state.myHeroName, state.myHealingPotions, state.myVisionPotions);
            case "Thief" -> new Thief(state.myHeroName, state.myHealingPotions, state.myVisionPotions);
            default -> throw new IllegalArgumentException("Invalid class choice: " + state.myHeroClass);
        }; 
        
        // Restore room states
        restoreRoomData(state.myRoomData, myDungeon);
        myDungeon.setEntrancePosition(state.myEntranceRow, state.myEntranceCol);
        myDungeon.setExitPosition(state.myExitRow, state.myExitCol);

        // Restore hero position in dungeon
        myDungeon.setHeroPosition(state.myHeroRow, state.myHeroCol);
        
        // Restore hero HP
        myHero.setHP(state.myHeroHP);
        
        // Restore hero pillars
        if (state.myPillarsFound != null) {
            for (final char pillar : state.myPillarsFound) {
                myHero.gainPillar(pillar);
            }
        }

        return new RestoreResult(myDungeon, myHero);
    }

    /**
     * Restores the state of all rooms from serialized room data.
     * 
     * @param roomData the 2D array of serialized room strings
     */
    private void restoreRoomData(final String[][] roomData, final Dungeon myDungeon) {
        final MonsterGenerator monsterGen = new MonsterGenerator();
        
        for (int row = 0; row < roomData.length; row++) {
            for (int col = 0; col < roomData[row].length; col++) {
                final String[] parts = roomData[row][col].split("\\|");
                final Room room = myDungeon.getRoom(row, col);
                
                if (parts.length >= 9) {
                    // Restore doors
                    room.setNorthDoor(parts[0].equals("1"));
                    room.setSouthDoor(parts[1].equals("1"));
                    room.setEastDoor(parts[2].equals("1"));
                    room.setWestDoor(parts[3].equals("1"));
                    
                    // Restore pillar
                    if (parts[4].equals("i")) {
                        room.setEntrance();
                    } else if (parts[4].equals("O")) {
                        room.setExit();
                    } else if (!parts[4].equals(".")) {
                        room.setPillar(parts[4].charAt(0));
                    }
                    
                    // Restore monster
                    if (!parts[5].equals("-")) {
                        final Monster monster = monsterGen.createMonster(parts[5]);
                        room.setMonster(monster);
                    }
                    
                    // Restore items
                    if (!parts[6].equals("none")) {
                        for (final String itemCode : parts[6].split(",")) {
                            if (itemCode.equals("H")) {
                                room.addItem(new HealingPotion());
                            } else if (itemCode.equals("V")) {
                                room.addItem(new VisionPotion());
                            }
                        }
                    }
                    
                    // Restore revealed state
                    room.setRevealed(parts[7].equals("1"));
                    
                    // Restore events
                    if (!parts[8].equals("none")) {
                        for (final String eventCode : parts[8].split(",")) {
                            switch (eventCode) {
                                case "P" -> room.addEvent(new PitEvent());
                                case "F" -> room.addEvent(new FountainEvent());
                                case "Po" -> room.addEvent(new PoisonEvent());
                                case "Al" -> room.addEvent(new AlarmEvent());
                            }
                        }
                    }
                }
            }
        }
    }
}