package ConwayGameOfLife;

import java.util.Random;

public class Universe {
    private int size;
    public static boolean[][] universe;
    public static boolean[][] nextGen;
    private static Random random;
    private static GameOfLifeInterface life;

    public Universe(int size, Random random, GameOfLifeInterface life) {
        this.size = size;
        Universe.random = random;
        universe = new boolean[size][size];
        nextGen = new boolean[size][size];
        Universe.life = life;
    }

    public int getSize() {
        return universe.length - 1;
    }

    public static int getTotalAlive(boolean[][] universe) {
        int counter = 0;

        for(int i = 0; i < universe.length; ++i) {
            for(int a = 0; a < universe[0].length; ++a) {
                if (universe[i][a]) {
                    ++counter;
                }
            }
        }

        life.setAlive(counter);
        return counter;
    }

    public GameOfLifeInterface getLife() {
        return life;
    }

    public static boolean[][] getUniverse() {
        return universe;
    }

    public static void setUniverse(boolean[][] uni) {
        universe = uni;
    }
}
