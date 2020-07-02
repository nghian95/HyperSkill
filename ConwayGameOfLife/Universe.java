package ConwayGameOfLife;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Universe {

    private int size;
    public static boolean[][] universe;
    public static boolean[][] nextGen;
    private static Random random;
    private static ConwayGameOfLife.GameOfLife life;

    public Universe(int size, Random random, ConwayGameOfLife.GameOfLife life) {
        this.size=size;
        this.random=random;
        universe = new boolean[size][size];
        nextGen = new boolean[size][size];
        this.life = life;
    }

    public int getSize(){
        return universe.length-1;
    }

    public static int getTotalAlive(boolean[][] universe) {
        int counter = 0;
        for (int i=0; i<universe.length; i++) {
            for (int a=0; a<universe[0].length; a++) {
                if (universe[i][a]) {
                    counter++;
                }
            }
        }
        life.setAlive(counter);
        return counter;
    }

    public GameOfLife getLife(){
        return life;
    }

    public static boolean[][] getUniverse() {
        return universe;
    }

}

