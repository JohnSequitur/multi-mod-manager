package io;

import gui.ManagerGUI;
import model.Game;
import model.Mod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

public class IOReader {

    static String path = IOWriter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    static final String folderName = "MMM_Files/";

    public static void readGames() {
        if (path.toCharArray()[2] == ':') {
            path = path.substring(1);
//            System.out.println(path);
        }
        Path dir = Path.of(path + folderName);
        try (Stream<Path> paths = Files.list(dir)) {
            paths.filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".mmm"))
                    .sorted(Comparator.comparing(Path::getFileName))
                    .forEach(file -> {
                System.out.println("Reading: " + file.getFileName());
                try (Scanner scanner = new Scanner(file)) {
                    Game game = new Game(scanner.nextLine(), scanner.nextLine(), scanner.nextLine());
                    while(scanner.hasNextLine()) {
                        game.addMod(new Mod(scanner.nextLine()));
                    }
                    ManagerGUI.games.add(game);
                }
                catch (IOException e) {
                    System.err.println("Failed to read " + file + ": " + e.getMessage());
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
