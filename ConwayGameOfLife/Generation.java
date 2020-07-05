package ConwayGameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Generation extends Thread{
    private static Universe univ;
    private static GameOfLife life;
    private static int time = 1000;
    private static Random random;
    private static Color aliveColor = Color.green;
    private static Color deadColor = Color.black;
    private static int z = 0;

    public Generation(Universe universe, GameOfLife life, Random random) {
        this.univ = universe;
        this.life = life;
        this.random = random;
    }

    public static void cellEvolution(int row, int column) {
        if (isAlive(row,column)) {
            if (adjacentAlive(row,column)==2 || adjacentAlive(row,column)==3){
                univ.nextGen[row][column]=true;
            } else {
                univ.nextGen[row][column]=false;
            }
        } else {
            if (adjacentAlive(row,column)==3){
                univ.nextGen[row][column]=true;
            } else {
                univ.nextGen[row][column]=false;
            }
        }
    }

    public static int adjacentAlive(int row, int column){
        int totalAlive = 0;
        int border = univ.getSize();
        boolean atLeft = column-1<0, atTop = row-1<0, atRight = column+1>border, atBottom = row+1>border;

        if (atBottom) {
            if (isAlive(0,column)) { //S
                totalAlive++;
            }
        } else {
            if (isAlive(row+1, column)) { //S
                totalAlive++;
            }
        }

        if (atTop) {
            if (isAlive(border,column)) { //N
                totalAlive++;
            }
        } else {
            if (isAlive(row-1, column)) { //N
                totalAlive++;
            }
        }

        if (atRight) {
            if (isAlive(row,0)){ //E
                totalAlive++;
            }
            if (atTop) {
                if (isAlive(border,0)) { //NE
                    totalAlive++;
                }
            } else {
                if (isAlive(row-1,0)) { //NE
                    totalAlive++;
                }
            }
            if (atBottom) {
                if (isAlive(0, 0)) { //SE
                    totalAlive++;
                }
            } else {
                if (isAlive(row+1,0)) { //SE
                    totalAlive++;
                }
            }
        } else {
            if (isAlive(row,column+1)){ //E
                totalAlive++;
            }
            if (atTop) {
                if (isAlive(border,column+1)) { //NE
                    totalAlive++;
                }
            } else {
                if (isAlive(row-1,column+1)) { //NE
                    totalAlive++;
                }
            }
            if (atBottom) {
                if (isAlive(0, column+1)) { //SW
                    totalAlive++;
                }
            } else {
                if (isAlive(row+1,column+1)) { //SW
                    totalAlive++;
                }
            }
        }

        if (atLeft){
            if (isAlive(row,border)){ //W
                totalAlive++;
            }
            if (atTop) {
                if(isAlive(border, border)) { //NW
                    totalAlive++;
                }
            } else {
                if (isAlive(row-1, border)) { //NW
                    totalAlive++;
                }
            }
            if (atBottom) {
                if (isAlive(0, border)) { //SW
                    totalAlive++;
                }
            } else {
                if (isAlive(row+1,border)) { //SW
                    totalAlive++;
                }
            }
        } else {
            if (isAlive(row,column-1)){ //W
                totalAlive++;
            }
            if (atTop) {
                if (isAlive(border,column-1)) { //NW
                    totalAlive++;
                }
            } else {
                if (isAlive(row-1, column-1)) { //NW
                    totalAlive++;
                }
            }
            if (atBottom) {
                if (isAlive(0, column-1)) { //SW
                    totalAlive++;
                }
            } else {
                if (isAlive(row+1,column-1)) { //SW
                    totalAlive++;
                }
            }
        }
        return totalAlive;
    }

    public static boolean isAlive(int row, int column){
        if (univ.universe[row][column]==true){
            return true;
        } else {
            return false;
        }
    }

    public static void setUniv() {
        univ.getLife().addGen();
        univ.getTotalAlive(univ.nextGen);
        for (int i=0; i<univ.getSize()+1;i++) {
            for (int a=0; a<univ.getSize()+1; a++) {
                univ.universe[i][a]=univ.nextGen[i][a];
                JPanel temp = new JPanel();
                if (univ.universe.length==Main.getSize()) {
                    if (univ.universe[i][a] == true) {
                        temp.setBackground(aliveColor);
                    } else {
                        temp.setBackground(deadColor);
                    }
                }
                univ.getLife().getGrid().add(temp);
            }
        }
        univ.getLife().getGrid().revalidate();
        univ.getLife().setLabels();
    }

    public static void refreshGridColor() {
        univ.getLife().removeComponents();
        for (int i=0; i<univ.getSize()+1;i++) {
            for (int a=0; a<univ.getSize()+1; a++) {
                JPanel temp = new JPanel();
                if (univ.universe[i][a]==true) {
                    temp.setBackground(aliveColor);
                } else {
                    temp.setBackground(deadColor);
                }
                univ.getLife().getGrid().add(temp);
            }
        }
        univ.getLife().getGrid().revalidate();
    }

    public static void initialGeneration() {
        for (int i = 0; i <= univ.getSize(); i++) {
            for (int a = 0; a <= univ.getSize(); a++) {
                univ.getUniverse()[i][a] = random.nextBoolean();
                JPanel temp = new JPanel();
                if (univ.getUniverse().length==Main.getSize()) {
                    if (Main.getGenThread().isInterrupted()) {
                        break;
                    }
                    if (univ.getUniverse()[i][a] == true) {
                        temp.setBackground(aliveColor);
                    } else {
                        temp.setBackground(deadColor);
                    }
                    life.getGrid().add(temp);
                }
            }
        }
        univ.getTotalAlive(univ.getUniverse());
        life.setLabels();
        life.getGrid().revalidate();
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                checkPaused();
                if (!life.getLoading()) {
                    initialGeneration();
                } else {
                    univ.setUniverse(life.getTempUni());
                }
                Main.getGenThread().sleep(time);
                restGen();
                //Main.getGenThread().sleep(time);
                return;
            } catch (InterruptedException e) {
                System.out.println("Sleeping was interrupted");
                return;
            }
        }
    }

    public static void restGen() throws InterruptedException {
        if (Main.getNumberOfGen()>0) {
            for (int b = z; b < Main.getNumberOfGen(); b++) {
                checkPaused();
                for (int i = 0; i< Main.getSize(); i++) {
                    for (int a = 0; a < Main.getSize(); a++) {
                        cellEvolution(i, a);
                        /*if (life.getGrid().getComponentCount()>0) {
                            life.getGrid().remove(0);
                        }*/
                    }
                }
                checkPaused();
                life.getGrid().removeAll();
                setUniv();
                Thread thread = Thread.currentThread();
                thread.sleep(time);

            }
        }

    }

    public static void checkPaused() throws InterruptedException{
        while (life.getPausedState()) {
            Thread.sleep(1000);
        }
    }

    public static void setTime(int t) {
        time = t;
    }

    public static void setAliveColor(Color color) {
        aliveColor = color;
    }

    public static Color getAliveColor(){
        return aliveColor;
    }

    public static void setDeadColor(Color color) {
        deadColor = color;
    }

    public static Color getDeadColor() {
        return deadColor;
    }

    public static void setZ(int ze) {
        z = ze;
    }
}
