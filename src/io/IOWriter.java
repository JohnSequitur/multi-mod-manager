package io;

import gui.ManagerGUI;
import model.Game;
import model.Mod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IOWriter {

    static String path = IOWriter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    static final String folderName = "MMM_Files/";

    public static void makeFolder() {
        if (path.toCharArray()[2] == ':') {
            path = path.substring(1);
//            System.out.println(path);
        }
        File folder = new File(path + folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }
    public static void writeGame(Game game) {
        if (path.toCharArray()[2] == ':') {
            path = path.substring(1);
//            System.out.println(path);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + folderName + game.getName() + ".mmm"))) {
            writer.write(game.getName());
            writer.newLine();
            writer.write(game.getFolderPath());
            writer.newLine();
            writer.write(game.getEnabledMod());
            writer.newLine();
            ArrayList<Mod> mods = game.getMods();
            for (Mod mod : mods) {
                writer.write(mod.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeAllGames() {
        for (Game game : ManagerGUI.games) {
            writeGame(game);
        }
    }
    public static void deleteGame(Game game) {
        if (path.toCharArray()[2] == ':') {
            path = path.substring(1);
//            System.out.println(path);
        }
        File file = new File(path + folderName + game.getName() + ".mmm");
        file.delete();
    }
}
