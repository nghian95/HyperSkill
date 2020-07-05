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
import java.io.FileWriter;
import java.io.IOException;
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
    private static boolean loading = false;
    private boolean[][] tempUni;
    private JTextField generationText;
    private JTextField sizeText;
    private FileWriter fileWriter;

    public GameOfLife() {
        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        setVisible(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setResizable(false);
    }

    public void initComponents(){
        leftPanel.setMaximumSize(new Dimension(200, 375));
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
        filePanel.setMaximumSize(new Dimension(200, 50));
        loadButton.addActionListener(e-> {
            int returnVal = fileC.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileC.getSelectedFile();
                //This is where a real application would open the file.
                try {
                    Scanner fileScan = new Scanner(file);
                    int newSize = fileScan.nextInt();
                    generationText.setText(String.valueOf(fileScan.nextInt())); //sets Main # of gens
                    for (ActionListener a : generationText.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                    }
                    generation = fileScan.nextInt();
                    tempUni = new boolean[newSize][newSize];
                    for (int i = 0; i < newSize; i++){
                        for (int n = 0; n < newSize; n++) {
                            tempUni[i][n] = fileScan.nextBoolean();
                        }
                    }
                    loading = true;
                    sizeText.setText(String.valueOf(newSize));
                    //Universe.setUniverse(tempUni);
                    Generation.setZ(generation);

                    for (ActionListener a : sizeText.getActionListeners()) {
                        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                    }

                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

            } else {
                System.out.println("Open command cancelled by user.");
            }
        });
        saveButton.addActionListener(e-> {
            int returnVal = fileC.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileC.getSelectedFile();
                //This is where a real application would open the file.
                paused = true;
                int tempSize = Main.getSize();
                boolean[][] tempUniv = Universe.getUniverse();
                String fileContent = tempSize + " " + ((int)Main.getNumberOfGen()+1) + " " + ((int)Generation.getZ()+1) + " ";
                for (int i = 0; i < tempSize; i++) {
                    for (int a = 0; a < tempSize; a++) {
                        fileContent += tempUniv[i][a] + " ";
                    }
                }
                System.out.println(fileContent);
                try {
                    fileWriter = new FileWriter(file);
                    fileWriter.append(fileContent);
                    fileWriter.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                paused=false;
            } else {
                System.out.println("Open command cancelled by user.");
            }
        });
        filePanel.add(Box.createRigidArea(new Dimension(2,0)));
        filePanel.add(loadButton);
        filePanel.add(saveButton);
        filePanel.setAlignmentX(LEFT_ALIGNMENT);
        leftPanel.add(filePanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setMaximumSize(new Dimension(200, 50));
        leftPanel.add(buttonPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(2,0)));
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(400, 30));
        leftPanel.add(separator);

        //leftPanel.add(generationLabel);
        generationLabel.setName("GenerationLabel");
        generationLabel.setAlignmentX(CENTER_ALIGNMENT);
        //generationLabel.setSize(200, 20);
        aliveLabel.setAlignmentX(CENTER_ALIGNMENT);
        //leftPanel.add(aliveLabel);
        JPanel labelPanel = new JPanel();
        labelPanel.add(generationLabel);
        labelPanel.add(Box.createRigidArea(new Dimension (0,15)));
        labelPanel.add(aliveLabel);
        labelPanel.setLayout(new BoxLayout(labelPanel,BoxLayout.Y_AXIS));
        JPanel anotherLabelPanel = new JPanel();
        //anotherLabelPanel.add(Box.createRigidArea(new Dimension (0,0)));
        anotherLabelPanel.add(labelPanel);
        anotherLabelPanel.setMaximumSize(new Dimension(200, 60));
        anotherLabelPanel.setAlignmentX(LEFT_ALIGNMENT);
        leftPanel.add(anotherLabelPanel);
        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setMaximumSize(new Dimension(400, 30));
        leftPanel.add(separator2);

        aliveLabel.setName("AliveLabel");

        JPanel generationPanel = new JPanel();
        JLabel changeGeneration = new JLabel("Desired Generations: ");
        generationPanel.add(changeGeneration);
        generationText = new JTextField("12");
        generationText.addActionListener(e -> {
            Main.setNumberOfGen(Integer.parseInt(generationText.getText())-1);
        });
        generationText.setMaximumSize(new Dimension(50,20));
        generationPanel.add(generationText);
        generationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        generationPanel.setMaximumSize(new Dimension(200,20));
        leftPanel.add(generationPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0,3)));

        JPanel sizePanel = new JPanel();
        JLabel changeSize = new JLabel("Desired Size: ");
        sizePanel.add(changeSize);
        sizeText = new JTextField("25");
        sizeText.addActionListener(e-> {
            Main.getGenThread().interrupt();
            Main.setSize(Integer.parseInt(sizeText.getText()));
            if (Main.getSize()==0) {
                Main.setSize(1);
                sizeText.setText("1");
            }
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
            if (!loading) {
                generation = 1;
            }
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
        sizePanel.setMaximumSize(new Dimension(200, 50));
        leftPanel.add(sizePanel);
        JSeparator separator3 = new JSeparator(SwingConstants.HORIZONTAL);
        separator3.setMaximumSize(new Dimension(400, 30));
        leftPanel.add(separator3);

        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
        buttonPanel.setAlignmentY(TOP_ALIGNMENT);
        buttonPanel.add(pauseResume);
        pauseResume.setName("PlayToggleButton");
        buttonPanel.add(resetButton);
        resetButton.setName("ResetButton");
        pauseResume.setHorizontalAlignment(SwingConstants.LEFT);

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
        evolutionSpeed.setAlignmentX(CENTER_ALIGNMENT);
        evolutionSpeed.setAlignmentY(TOP_ALIGNMENT);
        //evolutionSpeed.setPaintLabels(true);
        //leftPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JLabel evoSpeed = new JLabel("Change Evolution Speed: ");
        evoSpeed.setAlignmentX(CENTER_ALIGNMENT);
        JPanel evoSpeedPanel = new JPanel();
        evoSpeedPanel.setAlignmentX(LEFT_ALIGNMENT);
        evoSpeedPanel.setLayout(new BoxLayout(evoSpeedPanel, BoxLayout.Y_AXIS));
        evoSpeedPanel.add(evoSpeed);
        evoSpeedPanel.add(Box.createRigidArea(new Dimension(0,10)));
        evoSpeedPanel.add(evolutionSpeed);
        evoSpeedPanel.setMaximumSize(new Dimension(200, 70));
        evoSpeedPanel.add(Box.createRigidArea(new Dimension(0,10)));
        leftPanel.add(evoSpeedPanel);

        //aliveColor = new JColorChooser(Generation.getAliveColor());
        JButton colorButton = new JButton();
        colorButton.setText("Change Alive Color");
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Select a color", Generation.getAliveColor());
            Generation.setAliveColor(color);
            Generation.refreshGridColor();
        });
        colorButton.setAlignmentY(TOP_ALIGNMENT);
        JPanel updatedColorPanel = new JPanel();
        updatedColorPanel.setAlignmentX(LEFT_ALIGNMENT);
        updatedColorPanel.add(colorButton);

        JButton deadColorButton = new JButton();
        deadColorButton.setText("Change Dead Color");
        deadColorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Select a color", Generation.getDeadColor());
            Generation.setDeadColor(color);
            Generation.refreshGridColor();

        });
        deadColorButton.setAlignmentY(TOP_ALIGNMENT);
        updatedColorPanel.add(deadColorButton);
        updatedColorPanel.setLayout(new BoxLayout(updatedColorPanel,BoxLayout.Y_AXIS));
        updatedColorPanel.setMaximumSize(new Dimension(200,75));
        deadColorButton.setMaximumSize(new Dimension(200,30));
        colorButton.setMaximumSize(new Dimension(200,30));
        leftPanel.add(updatedColorPanel);

        addGrid();
    }

    public void reset(){
        Main.getGenThread().interrupt();
        if (grid.getComponentCount()>0) {
            removeComponents();
        }
        if (!loading) {
            generation = 1;
            Generation.setZ(1);
        }
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

    public static void setLoading (boolean b) {
        loading = b;
    }

}