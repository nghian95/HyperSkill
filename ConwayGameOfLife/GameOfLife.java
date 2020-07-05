package ConwayGameOfLife;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class GameOfLife extends JFrame {
    private int size = 0;
    private JPanel grid = new JPanel();
    private int generation = 1;
    private int alive = 0;
    private JLabel aliveLabel = new JLabel("Alive: " + alive);
    private JLabel generationLabel = new JLabel("Generation: #" + generation);
    private JToggleButton pauseResume = new JToggleButton("Pause");
    private JButton resetButton = new JButton("Reset");
    private boolean paused = false;
    private JPanel leftPanel = new JPanel();
    private JSlider evolutionSpeed = new JSlider(JSlider.HORIZONTAL,200,2200,1200);
    private static JColorChooser aliveColor;
    private GridLayout gridLayout;
    private boolean loading = false;
    private boolean[][] tempUni;
    private JTextField generationText;
    private JTextField sizeText;

    public GameOfLife() {
        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        setVisible(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setResizable(false);
    }

    public void initComponents(){
        leftPanel.setMaximumSize(new Dimension(200, 300));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setAlignmentY(TOP_ALIGNMENT);
        grid.setAlignmentY(TOP_ALIGNMENT);
        //leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(leftPanel);

        JFileChooser fileC = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter( "Text Files","txt");
        fileC.setFileFilter(filter);
        JButton loadButton = new JButton("Load");
        JButton saveButton = new JButton("Save");
        JPanel filePanel = new JPanel();
        loadButton.addActionListener(e-> {
            int returnVal = fileC.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileC.getSelectedFile();
                //This is where a real application would open the file.
                try {
                    Scanner fileScan = new Scanner(file);
                    Main.setSize(fileScan.nextInt());
                    Main.setNumberOfGen(fileScan.nextInt());
                    Generation.setZ(fileScan.nextInt());
                    size = Main.getSize();
                    tempUni = new boolean[size][size];
                    for (int i = 0; i < size; i++){
                        for (int n = 0; n < size; n++) {
                            tempUni[i][n] = fileScan.nextBoolean();
                        }
                    }
                    loading = true;
                    generationText.setText(String.valueOf(Main.getNumberOfGen()));
                    sizeText.setText(String.valueOf(size));
                    for (ActionListener a : generationText.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                    }
                    for (ActionListener a : sizeText.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                    }

                    Main.newThread();
                    if (Main.getGenThread().getState().equals(Thread.State.TERMINATED)) {
                        loading = false;
                    }
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

            } else {
                System.out.println("Open command cancelled by user.");
            }
        });
        saveButton.addActionListener(e -> {
            int returnVal = fileC.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileC.getSelectedFile();
                //This is where a real application would open the file.
                try {
                    Scanner fileScan = new Scanner(file);
                    Main.setSize(fileScan.nextInt());
                    Main.setNumberOfGen(fileScan.nextInt());
                    Generation.setZ(fileScan.nextInt());
                    size = Main.getSize();
                    tempUni = new boolean[size][size];
                    for (int i = 0; i < size; i++){
                        for (int n = 0; n < size; n++) {
                            tempUni[i][n] = fileScan.nextBoolean();
                        }
                    }
                    loading = true;
                    Main.newThread();
                    if (Main.getGenThread().getState().equals(Thread.State.TERMINATED)) {
                        loading = false;
                    }
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

            } else {
                System.out.println("Open command cancelled by user.");
            }
        });
        filePanel.add(loadButton);
        filePanel.add(saveButton);
        filePanel.setAlignmentX(LEFT_ALIGNMENT);
        leftPanel.add(filePanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
        leftPanel.add(generationLabel);
        generationLabel.setName("GenerationLabel");
        leftPanel.add(aliveLabel);
        leftPanel.add(buttonPanel);
        aliveLabel.setName("AliveLabel");

        JPanel generationPanel = new JPanel();

        JLabel changeGeneration = new JLabel("Desired Generations: ");
        generationPanel.add(changeGeneration);
        generationText = new JTextField("12");
        generationText.addActionListener(e -> {
            Main.setNumberOfGen(Integer.parseInt(generationText.getText())-1);
        });
        generationText.setMaximumSize(new Dimension(50,30));
        generationPanel.add(generationText);
        generationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        generationPanel.setMaximumSize(new Dimension(200,30));
        leftPanel.add(generationPanel);

        JPanel sizePanel = new JPanel();
        JLabel changeSize = new JLabel("Desired Size: ");
        sizePanel.add(changeSize);
        sizeText = new JTextField("25");
        sizeText.addActionListener(e-> {
            Main.getGenThread().interrupt();
            Main.setSize(Integer.parseInt(sizeText.getText()));
            size = Main.getSize();
            int gap = 25/size;
            this.remove(grid);
            grid = new JPanel();
            grid.setLayout(new GridLayout(size,size,gap,gap));
            grid.setAlignmentY(TOP_ALIGNMENT);
            this.add(grid);
            //reset();
            if (grid.getComponentCount()>0) {
                removeComponents();
            }
            generation=1;
            generationLabel.setText("Generation: #" + generation);
            alive = 0;
            aliveLabel.setText("Alive: " + alive);
            setLabels();
            this.revalidate();

            Main.newThread();
        });
        sizeText.setMaximumSize(new Dimension(40, 30));
        sizePanel.add(sizeText);
        sizePanel.setAlignmentX(LEFT_ALIGNMENT);
        leftPanel.add(sizePanel);

        buttonPanel.add(pauseResume);
        pauseResume.setName("PlayToggleButton");
        buttonPanel.add(resetButton);
        resetButton.setName("ResetButton");

        generationLabel.setAlignmentX(LEFT_ALIGNMENT);
        //generationLabel.setSize(200, 20);
        pauseResume.setHorizontalAlignment(SwingConstants.LEFT);
        aliveLabel.setAlignmentX(LEFT_ALIGNMENT);
        //aliveLabel.setSize(200, 20);

        pauseResume.addActionListener(e -> {
            if (!paused) {
                paused = true;
                //pauseResume.setText(" Play ");
            } else {
                paused = false;
                //pauseResume.setText("Pause");
            }
        });


        resetButton.addActionListener(e -> {
            reset();
        });

        evolutionSpeed.addChangeListener(e -> {
            Generation.setTime(evolutionSpeed.getValue());
        });
        evolutionSpeed.setMajorTickSpacing(200);
        evolutionSpeed.setPaintTicks(true);
        evolutionSpeed.setSnapToTicks(true);
        evolutionSpeed.setAlignmentX(LEFT_ALIGNMENT);
        evolutionSpeed.setAlignmentY(TOP_ALIGNMENT);
        //evolutionSpeed.setPaintLabels(true);
        leftPanel.add(evolutionSpeed);

        //aliveColor = new JColorChooser(Generation.getAliveColor());
        JButton colorButton = new JButton();
        colorButton.setText("Change Alive Color");
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Select a color", Generation.getAliveColor());
            Generation.setAliveColor(color);
            Generation.refreshGridColor();
        });
        colorButton.setAlignmentY(TOP_ALIGNMENT);
        leftPanel.add(colorButton);

        JButton deadColorButton = new JButton();
        deadColorButton.setText("Change Dead Color");
        deadColorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Select a color", Generation.getDeadColor());
            Generation.setDeadColor(color);
            Generation.refreshGridColor();

        });
        deadColorButton.setAlignmentY(TOP_ALIGNMENT);
        leftPanel.add(deadColorButton);

        addGrid();
    }

    public void reset(){
        Main.getGenThread().interrupt();
        if (grid.getComponentCount()>0) {
            removeComponents();
        }

        generation=1;
        generationLabel.setText("Generation: #" + generation);
        alive = 0;
        aliveLabel.setText("Alive: " + alive);
        setLabels();
        this.revalidate();

        Main.newThread();
    }

    public void addGrid(){
        size = Main.getSize();
        int gap = 25/size;
        gridLayout = new GridLayout(size, size, gap, gap);
        grid.setLayout(gridLayout);
        grid.setMaximumSize(new Dimension(1000,1000));
        grid.setName("grid");
        grid.setAlignmentX(LEFT_ALIGNMENT);
        this.add(grid);
    }

    public JPanel getGrid(){
        return grid;
    }

    public void addGen(){
        generation++;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getGen(){
        return generation;
    }

    public void setLabels() {
        aliveLabel.setText("Alive: " + alive);
        generationLabel.setText("Generation: #" + generation);
        aliveLabel.revalidate();
        generationLabel.revalidate();
    }

    public boolean getPausedState() {
        return paused;
    }

    public void removeComponents(){
        /*for (int i = 0; i < Universe.universe.length; i++) {
            for (int a = 0; a < Universe.universe[0].length; a++) {
                if (getGrid().getComponentCount()>0) {
                    getGrid().remove(0);
                }
            }
        }*/
        getGrid().removeAll();
    }

    public boolean getLoading() {
        return loading;
    }

    public boolean[][] getTempUni() {
        return tempUni;
    }

}