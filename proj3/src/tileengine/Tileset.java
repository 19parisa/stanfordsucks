package tileengine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile EMPTY = new TETile(' ', Color.black, Color.black, "empty");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile GRASS = new TETile('"', Color.green, Color.green, "grass", "tileengine/GRASS.jpg");
    public static final TETile BUSH = new TETile('w', Color.green, Color.green, "bush", "tileengine/BUSH2.jpg");
    public static final TETile YELLOWTILE = new TETile('D', Color.yellow, Color.yellow, "yellow tile");
    // public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
    //        "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile PINKTILE = new TETile('D', Color.pink, Color.pink, "pinktile");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black, "unlocked door");
    public static final TETile WHITETILE = new TETile('d', Color.white, Color.white, "whitetile");
    public static final TETile BLUETILE = new TETile('D', Color.blue, Color.blue, "bluetile");
    public static final TETile REDTILE = new TETile('D', Color.red, Color.red, "redtile");
    public static final TETile SECRET_PORTAL = new TETile('X', Color.orange, Color.black,
            "secret portal");
    public static final TETile OSKI = new TETile('0', Color.white, Color.white, "avatar", "tileengine/OSKI.jpg");
    public static final TETile AXE = new TETile('/', Color.red, Color.red, "axe", "tileengine/AXE.jpg");
    public static final TETile TREE = new TETile('♠', Color.green, Color.white, "tree", "tileengine/TREE.jpg");
}


