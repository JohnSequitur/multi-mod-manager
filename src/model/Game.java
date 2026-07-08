package model;

import gui.ManagerGUI;

import java.io.File;
import java.util.ArrayList;

public class Game {

    String name;
    String folderPath;
    ArrayList<Mod> mods = new ArrayList<Mod>();
    String enabledMod = "";

    static final String baseGame = "BASE";

    public Game(String name, String folderPath, String enabledMod) {
        setName(name);
        setFolderPath(folderPath);
        setEnabledMod(enabledMod);
    }

    public Game(String name, String folderPath) {
        this(name, folderPath, "");
    }

    public Game() {
        this("New Game", "New Game's Path");
    }

    public void addMod(Mod modToAdd) {
        for (Mod mod : mods) {
            if (mod.getName().equals(modToAdd.getName())) {
                throw new IllegalArgumentException("Mod name cannot be a duplicate.");
            }
        }
        File modPath = new File(folderPath + " [" + modToAdd.getName() + "]");
        if (!modToAdd.getName().equals(getEnabledMod())) {
            modPath.mkdir();
        }
        mods.add(modToAdd);
    }

    public void removeMod(Mod mod) {
        mods.remove(mod);
    }

    public void enableMod(Mod modToEnable) {

        if (!"".equals(enabledMod)) {
            File oldModDir = new File(folderPath);
            File oldModNewDir = new File(folderPath + " [" + enabledMod + "]");
            oldModDir.renameTo(oldModNewDir);

            enabledMod = modToEnable.getName();

            File newModDir = new File(folderPath + " [" + enabledMod + "]");
            File newModNewDir = new File(folderPath);
            newModDir.renameTo(newModNewDir);
        }
        else {
            File gameDir = new File(folderPath);
            File newGameDir = new File(folderPath + " [" + baseGame + "]");
            gameDir.renameTo(newGameDir);

            enabledMod = modToEnable.getName();

            File newModDir = new File(folderPath + " [" + enabledMod + "]");
            File newModNewDir = new File(folderPath);
            newModDir.renameTo(newModNewDir);
        }

    }

    public void disableMod() {
        File oldModDir = new File(folderPath);
        File oldModNewDir = new File(folderPath + " [" + enabledMod + "]");
        oldModDir.renameTo(oldModNewDir);

        enabledMod = "";

        File gameDir = new File(folderPath + " [" + baseGame + "]");
        File newGameDir = new File(folderPath);
        gameDir.renameTo(newGameDir);


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || "".equals(name)) {
            throw new IllegalArgumentException("Game name cannot be empty.");
        }
        for (Game game : ManagerGUI.games) {
            if (name.equals(game.getName())) {
                throw new IllegalArgumentException("Game name cannot be a duplicate.");
            }
        }
        this.name = name;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        if (folderPath == null || "".equals(folderPath)) {
            throw new IllegalArgumentException("Folder path cannot be empty.");
        }
        if (folderPath.toCharArray()[folderPath.length() - 1] == '/') {
            this.folderPath = folderPath.substring(0, folderPath.length() - 1);
        }
        else {
            this.folderPath = folderPath;
        }
    }

    public String getEnabledMod() {
        return enabledMod;
    }

    private void setEnabledMod(String enabledMod) {
        this.enabledMod = enabledMod;
    }

    public ArrayList<Mod> getMods() {
        return mods;
    }

    private void setMods(ArrayList<Mod> mods) {
        this.mods = mods;
    }

    @Override
    public String toString() {
        return name;
    }
}
