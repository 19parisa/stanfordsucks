package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;
import tileengine.TERenderer;

import java.awt.*;


/** Welcome to Stanford! Eat as many bushes as possible.
 */

public class stanfordWorld {
    public static final int WIDTH = 70; // used to be 80, 30
    public static final int HEIGHT = 35;
    public TETile[][] world;
    private TERenderer Ren;
    private TETile avatar = Tileset.OSKI; // Avatar (OSKI)
    private int aposX;
    private int aposY;
    private int numStudents;
    private static int numBushes;


    public stanfordWorld() {
        this.numStudents = 15;
        this.numBushes = 0;
        world = new TETile[WIDTH][HEIGHT];
        fillCampus();
        placeAvatar();
        moveAroundStanford();

    }

    public void fillCampus() {
        // Making grass
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.GRASS;
            }
        }

        // Making flowers
        for (int x = 1; x < WIDTH; x += 2) {
            for (int y = 1; y < HEIGHT; y += 2) {
                world[x][y] = Tileset.BUSH;
            }
        }

        // Making above white bar
        for (int w = 0; w < WIDTH; w++) {
            for (int h = HEIGHT - 2; h < HEIGHT; h++) {
                world[w][h] = Tileset.WHITETILE;
            }
        }
    }

    public void placeAvatar() {
        aposX = WIDTH / 2;
        aposY = HEIGHT / 2;
        world[aposX][aposY] = avatar;
    }


    static void makeInstructionBar() {
        Font font1 = new Font("Monaco", Font.BOLD, 12);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.textLeft(1, HEIGHT-1, "Surprise, you made it to Stanford's campus (boo)!" +
                " You have 10 seconds to make as much damage as possible to their campus!");
        // StdDraw.show();
    }

    static void exitStanford() {
        StdDraw.setCanvasSize(stanfordWorld.WIDTH * 16, stanfordWorld.HEIGHT * 16);
        StdDraw.setXscale(0, stanfordWorld.WIDTH);
        StdDraw.setYscale(0, stanfordWorld.HEIGHT);
        StdDraw.clear(StdDraw.WHITE);
        StdDraw.enableDoubleBuffering();

        Font f1 = new Font("Monaco", Font.ITALIC, 42);
        StdDraw.setFont(f1);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(stanfordWorld.WIDTH / 2, stanfordWorld.HEIGHT / 2, "congrats, you damaged "
                + stanfordWorld.numBushes + " bushes!");

        Font f2 = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setFont(f2);
        StdDraw.text(stanfordWorld.WIDTH / 2, stanfordWorld.HEIGHT / 2 - 4,
                "time to head back to berkeley!");
        StdDraw.show();

        // Sleep for 5 seconds
        try {
            Thread.sleep(5000);  // 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // world.drawHUD();
    }

    public void render() {
        Ren = new TERenderer();
        Ren.initialize(WIDTH, HEIGHT);
        Ren.renderFrame(world);
    }

    public static void main(String[] args) {
        new stanfordWorld();
    }

    public void moveAroundStanford() {
        StdDraw.pause(100);
        render();
        long tenSeconds = System.currentTimeMillis() + 10000; // 15 seconds moving around now

        while (System.currentTimeMillis() < tenSeconds) {
            char nextMove;
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                processKey(key);
            }
            Ren.renderFrame(world);
            makeInstructionBar();
            StdDraw.show();
        }

        exitStanford();
    }

    private void processKey(char key) {
        switch (Character.toLowerCase(key)) { // Lowercase to handle both cases
            case 'w':
                moveAvatar(0, 1); // Move up
                break;
            case 'a':
                moveAvatar(-1, 0); // Move left
                break;
            case 's':
                moveAvatar(0, -1); // Move down
                break;
            case 'd':
                moveAvatar(1, 0); // Move right
                break;
        }
    }

    private void moveAvatar(int deltaX, int deltaY) {
        int newX = aposX + deltaX;
        int newY = aposY + deltaY;

        if (isValidPosition(newX, newY)) {
            world[aposX][aposY] = Tileset.GRASS;
            aposX = newX;
            aposY = newY;
            world[aposX][aposY] = Tileset.OSKI;
        }
    }

    private boolean isValidPosition(int x, int y) {
        if (world[x][y] == Tileset.BUSH) {
            numBushes = numBushes + 1;
        }
        return (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT - 2 && world[x][y] != null);
    }
}
