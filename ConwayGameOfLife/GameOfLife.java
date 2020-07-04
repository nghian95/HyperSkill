package ConwayGameOfLife;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public GameOfLife() {
        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        setVisible(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
    }

    public void initComponents(){
        leftPanel.setMaximumSize(new Dimension(200, 300));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setAlignmentY(TOP_ALIGNMENT);
        grid.setAlignmentY(TOP_ALIGNMENT);
        //leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(leftPanel);
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
        JTextField generationText = new JTextField("12");
        generationText.addActionListener(e -> {
            Main.setNumberOfGen(Integer.parseInt(generationText.getText())-1);
        });
        generationText.setMaximumSize(new Dimension(40,30));
        generationPanel.add(generationText);
        generationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(generationPanel);

        JPanel sizePanel = new JPanel();
        JLabel changeSize = new JLabel("Desired Size: ");
        sizePanel.add(changeSize);
        JTextField sizeText = new JTextField("25");
        sizeText.addActionListener(e-> {
            Main.setSize(Integer.parseInt(sizeText.getText()));
            size = Main.getSize();
            int gap = 25/size;
            /*gridLayout.setHgap(gap);
            gridLayout.setVgap(gap);
            gridLayout.setColumns(size);
            gridLayout.setRows(size);
            grid.setLayout(new FlowLayout());*/
            this.remove(grid);
            grid = new JPanel();
            grid.setLayout(new GridLayout(size,size,gap,gap));
            this.add(grid);
            reset();
            //grid.setLayout(gridLayout);
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
        for (int i = 0; i < Universe.universe.length; i++) {
            for (int a = 0; a < Universe.universe[0].length; a++) {
                if (getGrid().getComponentCount()>0) {
                    getGrid().remove(0);
                }
            }
        }
    }

}