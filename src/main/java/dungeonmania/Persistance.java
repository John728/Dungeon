package dungeonmania;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Written by John Henderson
 */

/*
 * This class was first designed to save the current state of the dungeon
 * every tick in preparation for time travel. This was later changed to save
 * just the current dungeon.
 */

public class Persistance {
    
    private boolean hasSavedGame = false;
    private String saveFileName = "default.dat";
    private final static String SAVE_LOCATION= "src/game_saves/";

    private Dungeon currentDungeon;

    /**
     * @param dungeon - the dungeon you would like to save
     * @param saveName - the name of the save file
     * 
     * This method only needs to be called once. Once called, it will
     * save the dungeon list to a file.dat with the given filename. After this,
     * any other saveGame method can be called to save the dungeon.
     */
    public void saveGame(Dungeon dungeon, String saveName) {
        hasSavedGame = true;
        this.currentDungeon = dungeon;
        hardSaveGame(saveName);
    }

    /**
     * If this function is called after the user has saved the game previously,
     * it will hard save the most recent tick of the game with the same name as 
     * the previous save. If not, then it will preform nothing.
     */
    public void saveGame() {
        if (!hasSavedGame) return;
        hardSaveGame(saveFileName);
    }

    /**
     * @param saveName - the name of the save file
     * When this method is called, it saves the current dungeon list to a file.dat
     * with the given filename.
     */
    private void hardSaveGame(String saveName) {
        saveFileName = saveName;
        try (FileOutputStream gameSaves = new FileOutputStream(SAVE_LOCATION + saveName + ".dat");
            ObjectOutputStream newGameSave = new ObjectOutputStream(gameSaves)) {

            // write object to file
            newGameSave.writeObject(currentDungeon);

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error saving game, perhaps a new object type has been added to the dungeon?");
        }
    }

    /**
     * @param index the index of the dungeon you would like to load. The index 
     * starts from the end and moves towards the beginning.
     * @return the dungeon at the given index.
     * @throws IllegalArgumentException if the index is out of bounds.
     * 
     * This method will be primarily used for time travel.
     */
    public Dungeon getDungeon(int index) throws IllegalArgumentException {
        return currentDungeon;
    }

    // load dungeon list, and return latest
    public Dungeon loadGame(String saveName) throws IllegalArgumentException { 
        saveFileName = saveName;
        try (FileInputStream gameSave = new FileInputStream(new File(SAVE_LOCATION + saveName + ".dat"))) {
            
            ObjectInputStream dungeonFile = new ObjectInputStream(gameSave);
            
            this.currentDungeon = (Dungeon) dungeonFile.readObject();
            gameSave.close();
            dungeonFile.close();

        } catch (Exception ex) {
            throw new IllegalArgumentException("There was an error loading the game");
        } 

        return getDungeon(0);
    }

    /**
     * @return A list of names of saved games
     */
    public List<String> getSavedGames() {
        List<String> savedGames = new ArrayList<>();
        File folder = new File(SAVE_LOCATION);
        List<File> listOfFiles = new ArrayList<>(Arrays.asList(folder.listFiles()));
        listOfFiles.stream().filter(file -> file.getName().charAt(0) != '.')
                            .forEach(file -> savedGames.add(file.getName().replace(".dat", "")));
        return savedGames;
    }

    /*
     * Clears all dungeons from the save folder.
     */
    public static void clearDungeons() {
        File dir = new File(SAVE_LOCATION);
        for(File file: dir.listFiles()) {
            if (!file.isDirectory() & file.getName().charAt(0) != '.') {
                file.delete();
            }
        }
    }
}