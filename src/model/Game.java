package model;

import java.io.File;
import java.util.ArrayList;

public class Game {

    String name;
    String folderPath;
    ArrayList<Mod> mods = new ArrayList<Mod>();
    String enabledMod = "";

    static final String baseGame = "BASE";


    public Game(String name, String folderPath) {
        setName(name);
        setFolderPath(folderPath);
        if (folderPath.toCharArray()[folderPath.length() - 1] == '/') {
            setFolderPath(folderPath.substring(0, folderPath.length() - 1));
        }
    }

    public Game() {
        this("New Game", "New Game's Path");
    }

    public void addMod(Mod modToAdd) {
        // TODO: Don't allow duplicates
        File modPath = new File(folderPath + " [" + modToAdd.getName() + "]");
        modPath.mkdir();
        mods.add(modToAdd);
    }

    public void removeMod() {
        // TODO
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

    private void setName(String name) {
        if (name == null || "".equals(name)) {
            throw new IllegalArgumentException("Game name cannot be null or empty.");
        }
        this.name = name;
    }

    public String getFolderPath() {
        return folderPath;
    }

    private void setFolderPath(String folderPath) {
        if (folderPath == null || "".equals(folderPath)) {
            throw new IllegalArgumentException("Folder path cannot be null or empty.");
        }
        this.folderPath = folderPath;
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
