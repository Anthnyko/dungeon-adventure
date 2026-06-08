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
     * @param theState the GameState to restore from
     */
    public RestoreResult restoreGameState(final GameState theState) {
        // Initialize dungeon with saved dimensions
        final Dungeon myDungeon = new Dungeon(theState.myDungeonWidth, theState.myDungeonHeight, theState.myDungeonName, true);
        
        // Recreate hero from saved class
        final Hero myHero = switch (theState.myHeroClass) {
            case "Warrior" -> new Warrior(theState.myHeroName, theState.myHealingPotions, theState.myVisionPotions);
            case "Priest" -> new Priest(theState.myHeroName, theState.myHealingPotions, theState.myVisionPotions);
            case "Thief" -> new Thief(theState.myHeroName, theState.myHealingPotions, theState.myVisionPotions);
            default -> throw new IllegalArgumentException("Invalid class choice: " + theState.myHeroClass);
        }; 
        
        // Restore room states
        restoreRoomData(theState.myRoomData, myDungeon);
        myDungeon.setEntrancePosition(theState.myEntranceRow, theState.myEntranceCol);
        myDungeon.setExitPosition(theState.myExitRow, theState.myExitCol);

        // Restore hero position in dungeon
        myDungeon.setHeroPosition(theState.myHeroRow, theState.myHeroCol);
        
        // Restore hero HP
        myHero.setHP(theState.myHeroHP);
        
        // Restore hero pillars
        if (theState.myPillarsFound != null) {
            for (final char pillar : theState.myPillarsFound) {
                myHero.gainPillar(pillar);
            }
        }

        return new RestoreResult(myDungeon, myHero);
    }

    /**
     * Restores the state of all rooms from serialized room data.
     * 
     * @param theRoomData the 2D array of serialized room strings
     */
    private void restoreRoomData(final String[][] theRoomData, final Dungeon theDungeon) {
        final MonsterGenerator monsterGen = new MonsterGenerator();
        
        for (int row = 0; row < theRoomData.length; row++) {
            for (int col = 0; col < theRoomData[row].length; col++) {
                final String[] parts = theRoomData[row][col].split("\\|");
                final Room room = theDungeon.getRoom(row, col);
                
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
                            if (eventCode.equals("P")) {
                                room.addEvent(new PitEvent());
                            } else if (eventCode.equals("F")) {
                                room.addEvent(new FountainEvent());
                            } else if (eventCode.equals("Po")) {
                                room.addEvent(new PoisonEvent());
                            } else if (eventCode.equals("Al")) {
                                room.addEvent(new AlarmEvent());
                            }
                        }
                    }
                }
            }
        }
    }
}