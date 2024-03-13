
package core;
import java.awt.*;
import java.io.IOException;
import edu.princeton.cs.algs4.StdDraw;
import java.io.*;
import java.nio.file.*;


// steps
// 1. save most recent game without quit
// 2. create new world
// 3. run a for loop

public class GameLogic {
    private World world;

    public void runGame() throws IOException, ClassNotFoundException {
        boolean inMainMenu = true;
        long seed = System.currentTimeMillis();
        StringBuilder userInputBuffer = new StringBuilder();

        while (inMainMenu) {
            Menu.drawMainMenu();
            char userInput = Menu.waitForUserInput();

            switch (userInput) {
                //(1) YOU START NEW GAME (N) and THEN ":Q"
                case 'N':
                case 'n':
                    userInputBuffer.setLength(0);
                    clearTextFile();
                    long userSeed = getSeedFromUser();
                    world = new World(World.WIDTH, World.HEIGHT, userSeed); // Create the world with the user's seed
                    StdDraw.pause(500);
                    world.create();
                    inMainMenu = playGame(userInputBuffer);
                    break;

                // (2) YOU ALREADY QUITTED NOW YOU WANT TO LOAD A GAME
                case 'L':
                case 'l':
                    System.out.println("Trying to Load Recent Game");
                    String content = new String(Files.readAllBytes(Paths.get("/Users/oliviafan/Downloads/cs61b/" +
                            "fa23-proj3-g385/proj3/src/core/userInputFile.txt")));

                    // Separate the numeric and alphabetic parts
                    String[] parts = content.split("(?<=\\d)(?=\\D)");
                    long num = Long.parseLong(parts[0]);
                    char[] letterPart = parts[1].toCharArray();

                    world = new World(World.WIDTH, World.HEIGHT, num); // Create the world with the user's seed
                    StdDraw.pause(500);
                    world.create();

                    // run old game plays
                    for (char move : letterPart) {
                        handleMovement(move);
                    }
                    StringBuilder nextMoves = new StringBuilder();
                    inMainMenu = playGame(nextMoves);
                    break;

                // (3) QUITING (you clear the txt file and buffer) and end the Game
                case 'q':
                case 'Q':
                    userInputBuffer.setLength(0);
                    clearTextFile();
                    System.exit(0);
                    break;
            }
        }
    }


    private void clearTextFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/oliviafan/Downloads/cs61b/" +
                "fa23-proj3-g385/proj3/src/core/userInputFile.txt"))) {
            writer.write(""); // Write an empty string to the file, effectively clearing it
        }
    }

    private boolean playGame(StringBuilder nextMoves) throws IOException {
        boolean inMainMenu = false;
        boolean running = true;
        boolean quitting = false;

        while (running) {
            if (StdDraw.hasNextKeyTyped()) {
                char move = StdDraw.nextKeyTyped();
                if (move == ':') {
                    quitting = true;;
                }
                if ((move == 'Q' || move == 'q') && quitting) {
                    running = false;
                    inMainMenu = true;
                    quitting = false;
                    System.out.println("Quitting the Game To Save");
                    saveGame(nextMoves.toString());

                } else if ( "wasdWASD".indexOf(move) >= 0 ) {
                    handleMovement(move);
                    nextMoves.append(move);
                    running = true;
                }

            }

            String tileDescription = world.generateMouseLocation();
            world.drawHUD(tileDescription);
        }
        return inMainMenu;

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        GameLogic gameLogic = new GameLogic();
        gameLogic.runGame();
    }

    private long getSeedFromUser() throws IOException {
        boolean numberEntered = false;
        StdDraw.clear();
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(World.WIDTH / 2, World.HEIGHT / 2, "Enter a seed (start with N and then press" +
                " 'S' to submit):");
        StringBuilder seedString = new StringBuilder();
        while (true) {
            StdDraw.show();
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                if (Character.isDigit(key)) {
                    seedString.append(key);
                    numberEntered = true;
                }
                if ((key == 'S' || key == 's') && numberEntered) {
                    saveGame(seedString.toString());
                    break;
                }

                StdDraw.clear();  // Clear the canvas
                StdDraw.text(World.WIDTH / 2, World.HEIGHT / 2, "Enter a seed (start with N and then" +
                        " press 'S' to submit):");
                StdDraw.text(World.WIDTH / 2, World.HEIGHT / 2 - 4, seedString.toString()); // Display the current seed string
            }
        }

        try {
            return Long.parseLong(seedString.toString()); // Convert the string to a long
        } catch (NumberFormatException e) {
            return System.currentTimeMillis(); // Fallback to a default seed
        }
    }

    private void saveGame(String userInput) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/oliviafan/Downloads/cs61b/fa23-proj3-g385/proj3/src/core/userInputFile.txt", true))) {
            writer.write(userInput);
        }
    }

    private void handleMovement(char move) {
        switch (move) {
            case 'w':
            case 'W':
                world.moveOskiUp();
                break;
            case 'a':
            case 'A':
                world.moveOskiLeft();
                break;
            case 's':
            case 'S':
                world.moveOskiDown();
                break;
            case 'd':
            case 'D':
                world.moveOskiRight();
                break;
        }
    }
}

