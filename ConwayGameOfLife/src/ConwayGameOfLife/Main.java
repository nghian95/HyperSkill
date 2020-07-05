package ConwayGameOfLife;
import java.util.*;
import java.lang.Thread;

public class Main {
    private static int size = 25;
    private static int numberOfGen = 11; //scanner.nextInt();
    private static GameOfLife life = new GameOfLife();
    private static Thread generationT;
    private static Universe uni;

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        //size = scanner.nextInt();
        life.initComponents();
        newThread();
    }

    public static int getSize(){
        return size;
    }

    public static void setSize(int s) {
        size = s;
    }

    public static int getNumberOfGen(){
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
    }
}


