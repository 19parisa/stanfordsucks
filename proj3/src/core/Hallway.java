package core;
import tileengine.TETile;
import tileengine.Tileset;

public class Hallway {

    /** Make horizontal hallways. */
    public static void createHorizontalHallway(TETile[][] world, int yVal, int xValue, int endXValue) {
        createHorizontalPath(world, yVal, xValue, endXValue);
        createRightWall(world, yVal, endXValue);
        // ^ call helper method
    }

    private static void createHorizontalPath(TETile[][] world, int yVal, int xValue, int endXValue) {
        for (int i = xValue; i <= endXValue; i++) {
            boolean notEquals = world[i][yVal] != Tileset.WHITETILE;
            if (world[i][yVal] != Tileset.WHITETILE) {
                // Set horizontal path
                world[i][yVal] = Tileset.WHITETILE;
                createHorizontalWalls(world, yVal, i);
            }
        }
    }

    /** Creating the walls. */
    private static void createRightWall(TETile[][] world, int yVal, int xValue) {
        for (int yBound = -1; yBound <= 1; yBound++) {
            boolean notWall = world[xValue + 1][yVal + yBound] != Tileset.WHITETILE;

            if (world[xValue + 1][yVal + yBound] != Tileset.WHITETILE) {
                int newY = yVal + yBound;
                int addEnd = xValue + 1;
                // Make a right wall
                world[xValue + 1][yVal + yBound] = Tileset.GRASS;
            }
        }
    }

    private static void createHorizontalWalls(TETile[][] world, int yCoordinate, int xCoordinate) {
        for (int yBound = -1; yBound <= 1; yBound++) {
            boolean equals = world[xCoordinate][yCoordinate + yBound] != Tileset.WHITETILE;
            if (world[xCoordinate][yCoordinate + yBound] != Tileset.WHITETILE) {
                int newY = yCoordinate + yBound;
                world[xCoordinate][yCoordinate + yBound] = Tileset.GRASS;
            }
        }
    }

    /** Creating the hallways, vertically. */
    public static void createVerticalHallway(TETile[][] world, int xVal, int yVal, int endYValue) {
        for (int x = yVal; x <= endYValue; x++) {
            int newValue;
            for (int xBound = -1; xBound <= 1; xBound++) {
                int newYValue;
                int newXValue;
                boolean worldEquals = world[xVal + xBound][x] != Tileset.WHITETILE;

                if (world[xVal + xBound][x] != Tileset.WHITETILE) {
                    if (xBound == 0) {
                        world[xVal + xBound][x] = Tileset.WHITETILE;
                    } else {
                        world[xVal + xBound][x] = Tileset.GRASS;
                    }
                    // world[xVal + xBound][x] = (xBound == 0) ? Tileset.BLUETILE : Tileset.WALL;
                }
            }
        }
    }

}
