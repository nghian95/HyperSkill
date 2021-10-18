package ConwayGameOfLife;

import java.awt.Color;
import java.util.Random;
import javax.swing.JPanel;

public class Generation extends Thread {
    private static Universe univ;
    private static GameOfLife life;
    private static int time = 1000;
    private static Random random;
    private static Color aliveColor;
    private static Color deadColor;
    private static int z;

    public Generation(Universe universe, GameOfLife life, Random random) {
        univ = universe;
        Generation.life = life;
        Generation.random = random;
    }

    public static void cellEvolution(int row, int column) {
        Universe var10000;
        if (isAlive(row, column)) {
            if (adjacentAlive(row, column) != 2 && adjacentAlive(row, column) != 3) {
                var10000 = univ;
                Universe.nextGen[row][column] = false;
            } else {
                var10000 = univ;
                Universe.nextGen[row][column] = true;
            }
        } else if (adjacentAlive(row, column) == 3) {
            var10000 = univ;
            Universe.nextGen[row][column] = true;
        } else {
            var10000 = univ;
            Universe.nextGen[row][column] = false;
        }

    }

    public static int adjacentAlive(int row, int column) {
        int totalAlive = 0;
        int border = univ.getSize();
        boolean atLeft = column - 1 < 0;
        boolean atTop = row - 1 < 0;
        boolean atRight = column + 1 > border;
        boolean atBottom = row + 1 > border;
        if (atBottom) {
            if (isAlive(0, column)) {
                ++totalAlive;
            }
        } else if (isAlive(row + 1, column)) {
            ++totalAlive;
        }

        if (atTop) {
            if (isAlive(border, column)) {
                ++totalAlive;
            }
        } else if (isAlive(row - 1, column)) {
            ++totalAlive;
        }

        if (atRight) {
            if (isAlive(row, 0)) {
                ++totalAlive;
            }

            if (atTop) {
                if (isAlive(border, 0)) {
                    ++totalAlive;
                }
            } else if (isAlive(row - 1, 0)) {
                ++totalAlive;
            }

            if (atBottom) {
                if (isAlive(0, 0)) {
                    ++totalAlive;
                }
            } else if (isAlive(row + 1, 0)) {
                ++totalAlive;
            }
        } else {
            if (isAlive(row, column + 1)) {
                ++totalAlive;
            }

            if (atTop) {
                if (isAlive(border, column + 1)) {
                    ++totalAlive;
                }
            } else if (isAlive(row - 1, column + 1)) {
                ++totalAlive;
            }

            if (atBottom) {
                if (isAlive(0, column + 1)) {
                    ++totalAlive;
                }
            } else if (isAlive(row + 1, column + 1)) {
                ++totalAlive;
            }
        }

        if (atLeft) {
            if (isAlive(row, border)) {
                ++totalAlive;
            }

            if (atTop) {
                if (isAlive(border, border)) {
                    ++totalAlive;
                }
            } else if (isAlive(row - 1, border)) {
                ++totalAlive;
            }

            if (atBottom) {
                if (isAlive(0, border)) {
                    ++totalAlive;
                }
            } else if (isAlive(row + 1, border)) {
                ++totalAlive;
            }
        } else {
            if (isAlive(row, column - 1)) {
                ++totalAlive;
            }

            if (atTop) {
                if (isAlive(border, column - 1)) {
                    ++totalAlive;
                }
            } else if (isAlive(row - 1, column - 1)) {
                ++totalAlive;
            }

            if (atBottom) {
                if (isAlive(0, column - 1)) {
                    ++totalAlive;
                }
            } else if (isAlive(row + 1, column - 1)) {
                ++totalAlive;
            }
        }

        return totalAlive;
    }

    public static boolean isAlive(int row, int column) {
        Universe var10000 = univ;
        return Universe.universe[row][column];
    }

    public static void setUniv() {
        univ.getLife().addGen();
        Universe var10000 = univ;
        var10000 = univ;
        Universe.getTotalAlive(Universe.nextGen);

        for(int i = 0; i < univ.getSize() + 1; ++i) {
            for(int a = 0; a < univ.getSize() + 1; ++a) {
                var10000 = univ;
                boolean[] var3 = Universe.universe[i];
                Universe var10002 = univ;
                var3[a] = Universe.nextGen[i][a];
                JPanel temp = new JPanel();
                var10000 = univ;
                if (Universe.universe.length == Main.getSize()) {
                    var10000 = univ;
                    if (Universe.universe[i][a]) {
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

        for(int i = 0; i < univ.getSize() + 1; ++i) {
            for(int a = 0; a < univ.getSize() + 1; ++a) {
                JPanel temp = new JPanel();
                Universe var10000 = univ;
                if (Universe.universe[i][a]) {
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
        Universe var10000;
        for(int i = 0; i <= univ.getSize(); ++i) {
            for(int a = 0; a <= univ.getSize(); ++a) {
                var10000 = univ;
                Universe.getUniverse()[i][a] = random.nextBoolean();
                JPanel temp = new JPanel();
                var10000 = univ;
                if (Universe.getUniverse().length == Main.getSize()) {
                    if (Main.getGenThread().isInterrupted()) {
                        break;
                    }

                    var10000 = univ;
                    if (Universe.getUniverse()[i][a]) {
                        temp.setBackground(aliveColor);
                    } else {
                        temp.setBackground(deadColor);
                    }

                    life.getGrid().add(temp);
                }
            }
        }

        var10000 = univ;
        var10000 = univ;
        Universe.getTotalAlive(Universe.getUniverse());
        life.setLabels();
        life.getGrid().revalidate();
    }

    public void run() {
        if (!this.isInterrupted()) {
            try {
                checkPaused();
                if (!life.getLoading()) {
                    initialGeneration();
                } else {
                    Universe.setUniverse(life.getTempUni());
                    Universe.getTotalAlive(Universe.getUniverse());
                    life.setLabels();
                    refreshGridColor();
                    life.getGrid().revalidate();
                }

                Main.getGenThread();
                Thread.sleep((long)time);
                restGen();
                GameOfLife.setLoading(false);
            } catch (InterruptedException var2) {
                System.out.println("Sleeping was interrupted");
            }
        }

    }

    public static void restGen() throws InterruptedException {
        if (Main.getNumberOfGen() > 0) {
            if (!life.getLoading()) {
                z = 1;
            }

            for(int b = z; b <= Main.getNumberOfGen(); z = b++) {
                checkPaused();

                for(int i = 0; i < Main.getSize(); ++i) {
                    for(int a = 0; a < Main.getSize(); ++a) {
                        cellEvolution(i, a);
                    }
                }

                checkPaused();
                life.getGrid().removeAll();
                setUniv();
                Thread thread = Thread.currentThread();
                Thread.sleep((long)time);
            }
        }

    }

    public static void checkPaused() throws InterruptedException {
        while(life.getPausedState()) {
            Thread.sleep(1000L);
        }

    }

    public static void setTime(int t) {
        time = t;
    }

    public static void setAliveColor(Color color) {
        aliveColor = color;
    }

    public static Color getAliveColor() {
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

    public static int getZ() {
        return z;
    }

    static {
        aliveColor = Color.green;
        deadColor = Color.black;
        z = 1;
    }
}
