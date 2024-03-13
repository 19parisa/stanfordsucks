
package core;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long seed = System.currentTimeMillis();

        if (args.length > 0) {
            try {
                seed = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid seed format. Using the default seed.");
            }
        }
        World newWorld = new World(World.WIDTH, World.HEIGHT, seed);
        newWorld.create();
    }
}


