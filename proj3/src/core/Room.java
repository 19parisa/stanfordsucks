
package core;

public class Room {
    private int roomWidth;
    private int roomHeight;
    private int xCoord;
    private int yCoord;
    private int xMiddle;
    private int yMiddle;

    public Room(int w, int h, int xCoord, int yCoord) {
        this.roomWidth = w;
        this.roomHeight = h;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        // int doubleWidth = width * 2; DONT USE*
        // int doubleHeight = height * 2;
        this.xMiddle = (w + xCoord * 2) / 2;
        this.yMiddle = (h + yCoord * 2) / 2;
    }

    public int getXMiddle() {

        return xMiddle;
    }

    public int getYMiddle() {

        return yMiddle;
    }

    public int getDimension(String s) {
        if (s.equals("w")) {
            return roomWidth;
        }
        else {
            return roomHeight;
        }
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }
}
