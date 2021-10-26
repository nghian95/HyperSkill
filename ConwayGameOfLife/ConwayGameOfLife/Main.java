package ConwayGameOfLife;

import java.util.Random;

public class Main {
    private static int size = 25;
    private static int numberOfGen = 11;
    private static GameOfLife life = new GameOfLife();
    private static Thread generationT;
    private static Universe uni;

    public Main() {
    }

    public static void main(String[] args) {
        life.initComponents();
        newThread();
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int s) {
        size = s;
    }

    public static int getNumberOfGen() {
        return numberOfGen;
    }

    public static void setNumberOfGen(int num) {
        numberOfGen = num;
    }

    public static Thread getGenThread() {
        return generationT;
    }

    public static void newThread() {
        Random random = new Random();
        uni = new Universe(size, random, life);
        generationT = new Thread(new Generation(uni, life, random), "generation");
        generationT.start();
        System.out.println(Thread.activeCount());
    }
}
