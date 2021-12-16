package ConwayGameOfLife;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GameOfLifeInterface extends JFrame {
    private int size = 0;
    private JPanel grid = new JPanel();
    private GridLayout gridLayout;

    private JPanel leftPanel;
    private JLabel aliveLabel;
    private int alive = 0;
    private JLabel generationLabel;
    private int generation = 1;
    private JToggleButton pauseResume;
    private JButton resetButton;
    private JSlider evolutionSpeed;
    //private static JColorChooser aliveColor;

    private boolean paused;
    private static boolean loading = false;
    private boolean[][] tempUni;

    private JTextField generationText;
    private JTextField sizeText;

    private FileWriter fileWriter;

    public GameOfLifeInterface() {
        super("Game of Life");
        this.aliveLabel = new JLabel("Alive: " + this.alive);
        this.generationLabel = new JLabel("Generation: #" + this.generation);
        this.pauseResume = new JToggleButton("Pause");
        this.resetButton = new JButton("Reset");
        this.paused = false;
        this.leftPanel = new JPanel();
        this.evolutionSpeed = new JSlider(0, 400, 2000, 1200);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1200, 1000);
        this.setVisible(true);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        this.setResizable(false);
    }

    public void initComponents() {
        initFilePanel();
        initRunPanel();
        initDescriptorPanel();
        initEditPanel();
        initEvoSpeedPanel();
        initColorPanel();
        this.addGrid();
    }

    private void initColorPanel() {
        JButton colorButton = new JButton();
        colorButton.setText("Change Alive Color");
        colorButton.addActionListener((e) -> {
            Color color = JColorChooser.showDialog(this, "Select a color", Generation.getAliveColor());
            Generation.setAliveColor(color);
            Generation.refreshGridColor();
        });
        colorButton.setAlignmentY(0.0F);
        JPanel updatedColorPanel = new JPanel();
        updatedColorPanel.setAlignmentX(0.0F);
        updatedColorPanel.add(colorButton);
        JButton deadColorButton = new JButton();
        deadColorButton.setText("Change Dead Color");
        deadColorButton.addActionListener((e) -> {
            Color color = JColorChooser.showDialog(this, "Select a color", Generation.getDeadColor());
            Generation.setDeadColor(color);
            Generation.refreshGridColor();
        });
        deadColorButton.setAlignmentY(0.0F);
        updatedColorPanel.add(deadColorButton);
        updatedColorPanel.setLayout(new BoxLayout(updatedColorPanel, BoxLayout.Y_AXIS));
        updatedColorPanel.setMaximumSize(new Dimension(200, 75));
        deadColorButton.setMaximumSize(new Dimension(200, 30));
        colorButton.setMaximumSize(new Dimension(200, 30));
        this.leftPanel.add(updatedColorPanel);
    }

    private void initEvoSpeedPanel() {
        this.evolutionSpeed.addChangeListener((e) -> {
            Generation.setTime(this.evolutionSpeed.getValue());
        });
        this.evolutionSpeed.setMajorTickSpacing(200);
        this.evolutionSpeed.setPaintTicks(true);
        this.evolutionSpeed.setSnapToTicks(true);
        this.evolutionSpeed.setAlignmentX(0.5F);
        this.evolutionSpeed.setAlignmentY(0.0F);
        JLabel evoSpeed = new JLabel("Change Evolution Speed: ");
        evoSpeed.setAlignmentX(0.5F);
        JPanel evoSpeedPanel = new JPanel();
        evoSpeedPanel.setAlignmentX(0.0F);
        evoSpeedPanel.setLayout(new BoxLayout(evoSpeedPanel, 1));
        evoSpeedPanel.add(evoSpeed);
        evoSpeedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        evoSpeedPanel.add(this.evolutionSpeed);
        evoSpeedPanel.setMaximumSize(new Dimension(200, 70));
        evoSpeedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.leftPanel.add(evoSpeedPanel);
    }

    private void initEditPanel() {
        JPanel generationPanel = new JPanel();
        JLabel desiredGenerations = new JLabel("Desired Generations: ");
        generationPanel.add(desiredGenerations);
        this.generationText = new JTextField("12");
        this.generationText.addActionListener((e) -> {
            Main.setNumberOfGen(Integer.parseInt(this.generationText.getText()) - 1);
        });
        this.generationText.setMaximumSize(new Dimension(50, 20));
        generationPanel.add(this.generationText);
        generationPanel.setAlignmentX(0.0F);
        generationPanel.setMaximumSize(new Dimension(200, 20));
        this.leftPanel.add(generationPanel);

        this.leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        JPanel sizePanel = new JPanel();
        JLabel desiredSize = new JLabel("Desired Size: ");
        sizePanel.add(desiredSize);
        this.sizeText = new JTextField("25");
        this.sizeText.addActionListener((e) -> {
            Main.getGenThread().interrupt();
            int size = Integer.parseInt(this.sizeText.getText());
            if (size == 0 || size < 0) {
                Main.setSize(1);
                this.sizeText.setText("1");
            } else if (size > 40) {
                Main.setSize(40);
                this.sizeText.setText("40");
                JOptionPane.showMessageDialog(null, "Max size is 40 by 40.", "Max size limit reached", JOptionPane.PLAIN_MESSAGE);

            } else {
                Main.setSize(size);
            }

            this.size = Main.getSize();
            int gap = 25 / this.size;
            this.remove(this.grid);
            this.grid = new JPanel();
            this.grid.setLayout(new GridLayout(this.size, this.size, gap, gap));
            this.grid.setAlignmentY(0.0F);
            this.add(this.grid);
            if (this.grid.getComponentCount() > 0) {
                this.removeComponents();
            }

            if (!loading) {
                this.generation = 1;
            }

            this.generationLabel.setText("Generation: #" + this.generation);
            this.alive = 0;
            this.aliveLabel.setText("Alive: " + this.alive);
            this.setLabels();
            this.revalidate();
            Main.newThread();
        });
        this.sizeText.setMaximumSize(new Dimension(40, 30));
        sizePanel.add(this.sizeText);
        sizePanel.setAlignmentX(0.0F);
        sizePanel.setMaximumSize(new Dimension(200, 50));
        this.leftPanel.add(sizePanel);
        JSeparator separator3 = new JSeparator(0);
        separator3.setMaximumSize(new Dimension(400, 30));
        this.leftPanel.add(separator3);
    }

    private void initDescriptorPanel() {
        this.generationLabel.setName("GenerationLabel");
        this.generationLabel.setAlignmentX(0.5F);

        this.aliveLabel.setAlignmentX(0.5F);
        this.aliveLabel.setName("AliveLabel");

        JPanel descriptorPanel = new JPanel();
        descriptorPanel.add(this.generationLabel);
        descriptorPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        descriptorPanel.add(this.aliveLabel);
        descriptorPanel.setLayout(new BoxLayout(descriptorPanel, 1));

        JPanel outerDescriptorPanel = new JPanel();
        outerDescriptorPanel.add(descriptorPanel);
        outerDescriptorPanel.setMaximumSize(new Dimension(200, 60));
        outerDescriptorPanel.setAlignmentX(0.0F);
        this.leftPanel.add(outerDescriptorPanel);

        JSeparator separator2 = new JSeparator(0);
        separator2.setMaximumSize(new Dimension(400, 30));
        this.leftPanel.add(separator2);
    }

    private void initFilePanel() {
        this.leftPanel.setMaximumSize(new Dimension(200, 375));
        this.leftPanel.setLayout(new BoxLayout(this.leftPanel, 1));
        this.leftPanel.setAlignmentY(0.0F);
        this.grid.setAlignmentY(0.0F);
        this.add(this.leftPanel);
        JFileChooser fileC = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", new String[]{"txt"});
        fileC.setFileFilter(filter);
        JButton loadButton = new JButton("Load");
        JButton saveButton = new JButton("Save");
        JPanel filePanel = new JPanel();
        filePanel.setMaximumSize(new Dimension(200, 50));
        loadButton.addActionListener((e) -> {
            int returnVal = fileC.showOpenDialog(this);
            if (returnVal == 0) {
                File file = fileC.getSelectedFile();

                try {
                    Scanner fileScan = new Scanner(file);
                    int newSize = fileScan.nextInt();
                    this.generationText.setText(String.valueOf(fileScan.nextInt()));
                    ActionListener[] var7 = this.generationText.getActionListeners();
                    int var8 = var7.length;

                    int var9;
                    ActionListener a;
                    for(var9 = 0; var9 < var8; ++var9) {
                        a = var7[var9];
                        a.actionPerformed(new ActionEvent(this, 1001, (String)null));
                    }

                    this.generation = fileScan.nextInt();
                    this.getGridValues(fileScan, newSize);
                    loading = true;
                    this.sizeText.setText(String.valueOf(newSize));
                    Generation.setZ(this.generation);
                    var7 = this.sizeText.getActionListeners();
                    var8 = var7.length;

                    for(var9 = 0; var9 < var8; ++var9) {
                        a = var7[var9];
                        a.actionPerformed(new ActionEvent(this, 1001, (String)null));
                    }
                } catch (FileNotFoundException var11) {
                    var11.printStackTrace();
                }
            } else {
                System.out.println("Load command cancelled by user.");
            }

        });
        saveButton.addActionListener((e) -> {
            int returnVal = fileC.showSaveDialog(this);
            if (returnVal == 0) {
                File file = fileC.getSelectedFile();
                this.paused = true;
                int newGridSize = Main.getSize();
                boolean[][] tempUniv = Universe.getUniverse();
                String fileContent = newGridSize + " " + (Main.getNumberOfGen() + 1) + " " + (Generation.getZ() + 1) + " ";

                for(int i = 0; i < newGridSize; ++i) {
                    for(int a = 0; a < newGridSize; ++a) {
                        fileContent = fileContent + tempUniv[i][a] + " ";
                    }
                }

                try {
                    this.fileWriter = new FileWriter(file);
                    this.fileWriter.append(fileContent);
                    this.fileWriter.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }

                this.paused = false;
            } else {
                System.out.println("Save command cancelled by user.");
            }

        });
        filePanel.add(Box.createRigidArea(new Dimension(2, 0)));
        filePanel.add(loadButton);
        filePanel.add(saveButton);
        filePanel.setAlignmentX(0.0F);
        this.leftPanel.add(filePanel);
    }

    private void initRunPanel() {
        JPanel runPanel = new JPanel();
        runPanel.setMaximumSize(new Dimension(200, 50));
        this.leftPanel.add(runPanel);
        runPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        runPanel.setAlignmentX(0.0F);
        runPanel.setAlignmentY(0.0F);
        runPanel.add(this.pauseResume);
        this.pauseResume.setName("PlayToggleButton");
        runPanel.add(this.resetButton);
        this.resetButton.setName("ResetButton");
        this.pauseResume.setHorizontalAlignment(2);
        this.pauseResume.addActionListener((e) -> {
            if (!this.paused) {
                this.paused = true;
            } else {
                this.paused = false;
            }
        });
        this.resetButton.addActionListener((e) -> {
            this.reset();
        });
        JSeparator separator = new JSeparator(0);
        separator.setMaximumSize(new Dimension(400, 30));
        this.leftPanel.add(separator);
    }

    private void getGridValues(Scanner fileScan, int newSize) {
        this.tempUni = new boolean[newSize][newSize];

        for(int i = 0; i < newSize; ++i) {
            for(int n = 0; n < newSize; ++n) {
                this.tempUni[i][n] = fileScan.nextBoolean();
            }
        }

    }

    public void reset() {
        Main.getGenThread().interrupt();
        if (this.grid.getComponentCount() > 0) {
            this.removeComponents();
        }

        if (!loading) {
            this.generation = 1;
            Generation.setZ(1);
        }

        this.generationLabel.setText("Generation: #" + this.generation);
        this.alive = 0;
        this.aliveLabel.setText("Alive: " + this.alive);
        this.setLabels();
        this.revalidate();
        Main.newThread();
    }

    public void addGrid() {
        this.size = Main.getSize();
        int gap = 25 / this.size;
        this.gridLayout = new GridLayout(this.size, this.size, gap, gap);
        this.grid.setLayout(this.gridLayout);
        this.grid.setMaximumSize(new Dimension(1000, 1000));
        this.grid.setName("grid");
        this.grid.setAlignmentX(0.0F);
        this.add(this.grid);
    }

    public JPanel getGrid() {
        return this.grid;
    }

    public void addGen() {
        ++this.generation;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public int getGen() {
        return this.generation;
    }

    public void setLabels() {
        this.aliveLabel.setText("Alive: " + this.alive);
        this.generationLabel.setText("Generation: #" + this.generation);
        this.aliveLabel.revalidate();
        this.generationLabel.revalidate();
    }

    public boolean getPausedState() {
        return this.paused;
    }

    public void removeComponents() {
        this.getGrid().removeAll();
    }

    public boolean getLoading() {
        return loading;
    }

    public boolean[][] getTempUni() {
        return this.tempUni;
    }

    public static void setLoading(boolean b) {
        loading = b;
    }
}
