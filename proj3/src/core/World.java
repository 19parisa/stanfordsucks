package core;

import java.awt.*;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import edu.princeton.cs.algs4.StdDraw;
import static utils.RandomUtils.uniform;
import static java.lang.Math.min;

public class World {
    public static final int WIDTH =  70;
    public static final int HEIGHT = 35;
    private int width;
    private int height;
    private long seed;
    private int numRooms;
    private int oskiXCoord;
    private int oskiYCoord;
    private int axesCollected;
    private int axesLeftToCollect;
    private boolean isPortalCreated;

    private boolean gameOver; // change
    private Random RANDOM;
    public TETile[][] world;
    private ArrayList<Room> roomList = new ArrayList<>();
    TERenderer ter = new TERenderer();
    private int portalxCoord;
    private int portalYCoord;
    private int numTreesCollected; // to count the number of trees collected
    private int numLives = 3; // initial number of lives



    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.RANDOM = new Random(seed);
        this.world = new TETile[width][height];
        this.numRooms = uniform(RANDOM, 20, 40);
        this.axesCollected = 0;
        this.axesLeftToCollect = 10;
        this.isPortalCreated = false;
    }



    public TETile[][] getWorldTiles() {
        return this.world; // Return the 2D array representing the tiles
    }

    public void createEmpty() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.world[x][y] = Tileset.EMPTY;
            }
        }
    }

    private void createRoom(Room room) {
        if (!isSpaceAvailableForRoom(room)) {
            return; // exit if no space
        }

        roomList.add(room);
        setRoomWalls(room);
        fillRoomInterior(room);
    }
    public void createPortal() {
        portalxCoord = uniform(RANDOM, 1, width);
        portalYCoord = uniform(RANDOM, 1, height);
        boolean portalValid =  (!(world[portalxCoord][portalYCoord] == Tileset.WHITETILE) ||
                (world[portalxCoord][portalYCoord] == Tileset.OSKI));

        while (!(world[portalxCoord][portalYCoord] == Tileset.WHITETILE) || (world[portalxCoord][portalYCoord]
                == Tileset.OSKI)) {
            if (portalxCoord < width - 1) {
                portalxCoord += 1;
            } else {
                portalxCoord = 1;
                if (portalYCoord < height - 1) {
                    portalYCoord += 1;
                } else {
                    portalYCoord = 1;
                }
            }
        }

        world[portalxCoord][portalYCoord] = Tileset.SECRET_PORTAL;
    }


    private boolean isSpaceAvailableForRoom(Room room) {
        int roomDim = room.getXCoord() + room.getDimension("w") + 1;
        int minValue = min(width, room.getXCoord() + room.getDimension("w") + 1);
        for (int x = room.getXCoord() - 1; x < min(width, room.getXCoord() + room.getDimension("w") + 1); x++) {
            int newCoordinate = room.getYCoord() - 1;
            int newDimension = room.getYCoord() + room.getDimension("h") + 1;
            for (int y = room.getYCoord() - 1; y < min(height, room.getYCoord() + room.getDimension("h") + 1); y++) {
                boolean equals = world[x][y] == Tileset.WHITETILE || world[x][y] == Tileset.GRASS;
                if (world[x][y] == Tileset.WHITETILE || world[x][y] == Tileset.GRASS) {
                    return false; // no space available
                }
            }
        }
        return true; // space is available
    }

    private void setRoomWalls(Room room) {
        // Set horizontal walls
        int dimension = room.getXCoord() + room.getDimension("w") + 1;
        int newCoord = room.getXCoord() - 1;
        for (int r = room.getXCoord() - 1; r < room.getXCoord() + room.getDimension("w") + 1; r++) {
            int newDim = room.getYCoord() + room.getDimension("h");
            world[r][room.getYCoord() + room.getDimension("h")] = Tileset.GRASS;
            int yCoord = room.getYCoord() - 1;
            world[r][room.getYCoord() - 1] = Tileset.GRASS;
            world[r][room.getYCoord() - 1] = Tileset.GRASS;
        }

        // make vertical walls
        int verticalDimension = room.getYCoord() + room.getDimension("h") + 1;
        for (int r = room.getYCoord() - 1; r < room.getYCoord() + room.getDimension("h") + 1; r++) {
            int worldIndex = room.getXCoord() + room.getDimension("w");
            int xCoordinate = room.getXCoord();
            int yCoordinate = room.getYCoord();
            world[room.getXCoord() + room.getDimension("w")][r] = Tileset.GRASS;
            world[room.getXCoord() - 1][r] = Tileset.GRASS;
        }
    }

    private void fillRoomInterior(Room room) {
        int xCoord = room.getXCoord();
        int dimension = room.getXCoord() + room.getDimension("w");
        for (int x = room.getXCoord(); x < room.getXCoord() + room.getDimension("w"); x++) {
            int interior = room.getYCoord() + room.getDimension("h");
            int yCoord = room.getYCoord();
            for (int y = room.getYCoord(); y < room.getYCoord() + room.getDimension("h"); y++) {
                world[x][y] = Tileset.WHITETILE;
            }
        }
    }

    private void createHallways(int size) {
        int hallwaysSize = size - 1;

        for (int i = 0; i < size - 1; i++) {
            ArrayList<Room> newList = new ArrayList<>();
            boolean connected;
            newList.add(roomList.get(i));
            newList.add(roomList.get(i + 1));
            connectRoomsTogether(newList);
        }
    }

    private void connectRoomsTogether(ArrayList<Room> rooms) {
        Room room1 = rooms.get(0);
        Room room2 = rooms.get(1);

        int x1 = room1.getXMiddle();
        int y1 = room1.getYMiddle();
        int x2 = room2.getXMiddle();
        int y2 = room2.getYMiddle();

        if (x1 < x2) {
            Hallway.createHorizontalHallway(world, y1, x1, x2);
        } else {
            Hallway.createHorizontalHallway(world, y1, x2, x1);
        }

        if (y1 < y2) {
            Hallway.createVerticalHallway(world, x2, y1, y2);
        } else {
            Hallway.createVerticalHallway(world, x2, y2, y1);
        }
    }

    private List<Integer> generateRandomXCoordinates(int numRooms) {
        List<Integer> yCoordinateList = new ArrayList<>();
        List<Integer> xCoordinateList = new ArrayList<>();
        int num = numRooms + 1;
        for (int i = 0; i < numRooms; i++) {
            int newWidth = width - 1;
            int xCoordinate = uniform(RANDOM, 1, width - 1);
            xCoordinateList.add(xCoordinate);
        }
        Collections.sort(xCoordinateList);
        return xCoordinateList;
    }

    private void generateRandomRoom(int xValue) {
        int newWidth = width - xValue;
        int randomWidth = uniform(RANDOM, 1, min(15, width - xValue));
        int randomHeight = uniform(RANDOM, 5, 10);
        int newHeight = height - randomHeight - 4;
        int room2 = uniform(RANDOM, 1, height - randomHeight - 4);

        Room newRoom = new Room(randomWidth, randomHeight, xValue, room2);
        // Call createRoom helper method
        createRoom(newRoom);
    }

    private void createRandomRooms() {
        List<Integer> xCoordinateList = generateRandomXCoordinates(numRooms);
        List<Integer> yCoordinateList = new ArrayList<>();

        for (int i : xCoordinateList) {
            generateRandomRoom(i);
        }
    }

    public void create() {

        createEmpty();
        createRandomRooms();
        // generateMouseLocation();
        createHallways(roomList.size());
        placeAvatar();
        createTrees();
        createAxes();
        createPortal();

        long seed = System.currentTimeMillis();
        long seed2 = Long.parseLong(Long.toString(seed));

        new World(WIDTH, HEIGHT, seed2);
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);

        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }




    /** Project 3B & 3C */
    public void placeAvatar() {
        oskiXCoord = uniform(RANDOM, 1, width);
        oskiYCoord = uniform(RANDOM, 1, height);
        while (!(world[oskiXCoord][oskiYCoord] == Tileset.WHITETILE)) {
            if (oskiXCoord < width - 1) {
                oskiXCoord += 1;
            } else {
                oskiXCoord = 1;
                if (oskiYCoord < height - 1) {
                    oskiYCoord += 1;
                } else {
                    oskiYCoord = 1;
                }
            }
        }
        world[oskiXCoord][oskiYCoord] = Tileset.OSKI;
    }

    /** Create 10 axes at random locations for Oski to collect! */
    private void createAxes() {
        while (axesLeftToCollect > 0) {
            int xCoordinate = randomAxeHelper(1, width);
            int yCoordinate = randomAxeHelper(1, height);

            if (treeExists(xCoordinate, yCoordinate)) {
                continue; // if tree exists at location --> BREAK
            }

            if (whiteTileExists(xCoordinate, yCoordinate)) { // else if white tile --> replace with axe
                placeAxe(xCoordinate, yCoordinate);
                axesLeftToCollect--;
            }
        }
    }

    private int randomAxeHelper(int min, int max) {
        return uniform(RANDOM, min, max);
    }

    private boolean treeExists(int x, int y) {
        return world[x][y] == Tileset.TREE;
    }

    private boolean whiteTileExists(int x, int y) {
        return world[x][y] == Tileset.WHITETILE;
    }

    private void placeAxe(int x, int y) {
        world[x][y] = Tileset.AXE;
    }

    /** Create trees randomly plants trees for Oski to collect! */
    private void createTrees() {
        int treesRemaining = numRooms / 4;
        while (treesRemaining > 0) {
            int xCoordinate = randomTreeHelper(1, width);
            int yCoordinate = randomTreeHelper(1, height);

            if (axeExists(xCoordinate, yCoordinate)) {
                continue; // Skip to the next iteration if an axe is found.
            }

            if (whiteTileExists(xCoordinate, yCoordinate)) {
                plantTree(xCoordinate, yCoordinate);
                treesRemaining--;
            }
        }
    }

    private int randomTreeHelper(int min, int max) {
        return uniform(RANDOM, min, max);
    }

    private boolean axeExists(int x, int y) {
        return world[x][y] == Tileset.AXE;
    }

    private void plantTree(int x, int y) {
        world[x][y] = Tileset.TREE;
    }



    public void drawHUD(String tileDescription) {
        StdDraw.setPenColor(StdDraw.BLACK); // Use the background color of your HUD
        StdDraw.filledRectangle(this.width / 2, this.height - 1, this.width / 2, 1);

        StdDraw.setPenColor(Color.WHITE);
        Font smallFont = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(smallFont);
        StdDraw.line(0, this.height - 2, this.width, this.height - 2);

        // Display Axes Collected
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.textLeft(1, this.height - 1, "Axes Collected: " + axesCollected);

        // Display Trees Collected
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.textLeft(15, this.height - 1, "Trees Collected: " + numTreesCollected);

        // Display Lives
        StdDraw.setPenColor(Color.RED);
        StdDraw.textLeft(30, this.height - 1, "Lives: " + numLives);

        // Display the description of the tile under the mouse pointer
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textRight(this.width - 1, this.height - 1, tileDescription);

//        StdDraw.show();
        StdDraw.show();
    }

    public String generateMouseLocation() {
        int mouseXPosition = (int) StdDraw.mouseX(); // mouse position
        int mouseYPosition = (int) StdDraw.mouseY();

        if (mouseXPosition < 0 || mouseXPosition >= width || mouseYPosition < 0 || mouseYPosition >= height) { // if mouse is within bound
            return ""; // return empty if outside world
        }

        TETile mouseTile = world[mouseXPosition][mouseYPosition];

        // Tile descriptions
        if (mouseTile == Tileset.WHITETILE) {
            return "Welcome to Berkeley's Campus!!!";
        } else if (mouseTile == Tileset.OSKI) {
            return "This is you, Oski!";
        } else if (mouseTile == Tileset.TREE) {
            return "I'm a Tree! Don't run into me!";
        } else if (mouseTile == Tileset.AXE) {
            return "I'm an axe, collect me to CHOP TREES!";
        } else if (mouseTile == Tileset.GRASS) {
            return "Just grass surrounding campus.";
        } else if (mouseTile == Tileset.EMPTY) {
            return "This is the wild, unexplored area outside of campus.";
        } else if (mouseTile == Tileset.SECRET_PORTAL) {
            return "Ooh a secret hole? Come explore!";
        } else {
            return ""; // default cases
        }
    }

    public TETile[][] getWorld() {
        return world;
    }

    /*  OSKI MOVEMENTS AND GAMEPLAY */

    public void moveOskiUp() {
        if (oskiYCoord < height - 1 && isValidMove(oskiXCoord, oskiYCoord + 1)) {
            updateOskiPosition(oskiXCoord, oskiYCoord + 1);
        }

    }

    public void moveOskiDown() {
        if (oskiYCoord > 0 && isValidMove(oskiXCoord, oskiYCoord - 1)) {
            updateOskiPosition(oskiXCoord, oskiYCoord - 1);
        }
    }

    public void moveOskiLeft() {
        if (oskiXCoord > 0 && isValidMove(oskiXCoord - 1, oskiYCoord)) {
            updateOskiPosition(oskiXCoord - 1, oskiYCoord);
        }
    }

    public void moveOskiRight() {
        if (oskiXCoord < width - 1 && isValidMove(oskiXCoord + 1, oskiYCoord)) {
            updateOskiPosition(oskiXCoord + 1, oskiYCoord);
        }
    }

    private boolean isValidMove(int x, int y) {
        return world[x][y] != Tileset.GRASS || world[x][y] == Tileset.SECRET_PORTAL;
        // return world[x][y] != Tileset.GRASS && world[x][y] != Tileset.TREE;
    }


    private void updateOskiPosition(int newX, int newY) {
        TETile newPositionTile = world[newX][newY];

        if (newPositionTile == Tileset.SECRET_PORTAL) {
            stanfordWorld sw = new stanfordWorld();
            // StdDraw.pause(10000);
            // return;

        } else if (newPositionTile == Tileset.TREE) {
            numTreesCollected++;
            numLives--;
            if (numLives <= 0) {
                gameOver = true;
            }
        } else if (newPositionTile == Tileset.AXE) {
            axesCollected++;
        }
        /**if (world[oskiXCoord][oskiYCoord] != Tileset.SECRET_PORTAL) { // new
            world[oskiXCoord][oskiYCoord] = Tileset.WHITETILE;
        }*/


        world[oskiXCoord][oskiYCoord] = Tileset.WHITETILE;
        oskiXCoord = newX;
        oskiYCoord = newY;
        world[oskiXCoord][oskiYCoord] = Tileset.OSKI;

        ter.renderFrame(world);
    }


}
