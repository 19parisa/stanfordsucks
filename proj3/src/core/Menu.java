package core;
import java.awt.*;
import edu.princeton.cs.algs4.StdDraw;

public class Menu {
    private static World ourWorld; // CHANGE
    public Menu() {
        // Initialize the menu
    }

    public void welcomeMenu() {
        // Implement the welcome menu logic
    }

    public static void drawMainMenu() {
        StdDraw.setCanvasSize(World.WIDTH * 16, World.HEIGHT * 16);
        StdDraw.setXscale(0, World.WIDTH);
        StdDraw.setYscale(0, World.HEIGHT);
        StdDraw.clear(StdDraw.WHITE);
        StdDraw.enableDoubleBuffering();

        String imagePath = "tileengine/CAL.png"; // Replace with your image path
        StdDraw.picture(World.WIDTH / 2, World.HEIGHT / 2 + 7, imagePath);

        Font font1 = new Font("Monaco", Font.ITALIC, 45);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(World.WIDTH / 2, World.HEIGHT / 2, "WELCOME TO YUCK STANFORD! The Game!");

        Font font2 = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setFont(font2);
        StdDraw.text(World.WIDTH / 2, World.HEIGHT / 2 - 4, "New Game (N)");
        StdDraw.text(World.WIDTH / 2, World.HEIGHT / 2 - 6, "Load Saved Game (L)"); // Doesnt work yet
        StdDraw.text(World.WIDTH / 2, World.HEIGHT / 2 - 8, "Quit Game (Q)"); // doesnt work yet
        StdDraw.show();
    }

    static char waitForUserInput() {
        while (!StdDraw.hasNextKeyTyped()) {
            int hello;
        }
        return StdDraw.nextKeyTyped();
    }
}
