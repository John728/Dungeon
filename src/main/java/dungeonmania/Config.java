package dungeonmania;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dungeonmania.util.FileLoader;

/*
 * Written by John Henderson
 */

 /*
  * This class was created to allow users to access the config settings from anywhere in the code base.
  * When the game is loaded in, call the load config setting, and it will store a JsonObject of the config
  * settings to be accessed anywhere.
  */

/*
 * Examples:
 * int player_health = Config.getSetting(Settings.player_health); // this will return the players health
 * int player_attack = Config.getSetting(Settings.player_attack); // this will return the players attack
 */

public class Config {

    private static JsonObject configSettings;

    // Should fix the throws, but for now, is okay
    public static void loadConfig(String configName) throws IllegalArgumentException {

        // handle later
        try {
            Config.configSettings = JsonParser.parseString(FileLoader.loadResourceFile("/configs/" + configName + ".json")).getAsJsonObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Config file not found");
        } 
    }

    /**
     * @param setting The setting to get
     * @return the value of the setting as a String
     * @pre setting is a valid setting. 
     * @author John Henderson
     * 
     * Assumes the setting is correct, and does not check. It is better to use the enum
     * than to type the setting name.
     * 
     * Example:
     * Config.getSetting(Settings.player_health); // this will return the players health
     * Config.getSetting(Settings.player_attack); // this will return the players attack
     * ect
     */
    public static int getSetting(Settings setting) {
        return configSettings.get(setting.toString()).getAsInt();
    }

    public static double getSettingAsDouble(Settings setting) {
        return configSettings.get(setting.toString()).getAsDouble();
    }
}
