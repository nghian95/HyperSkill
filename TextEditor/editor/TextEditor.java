package editor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JTextField searchField = new JTextField();
    private JTextArea textArea;
    private JFileChooser fileChooser = new JFileChooser("resources");
    private FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Text Files","txt");
    private ArrayDeque<Integer> searchIndexes = new ArrayDeque<>();
    private ArrayDeque<Integer> indexPlusLengths = new ArrayDeque<>();
    private JCheckBox check = new JCheckBox();

    public TextEditor() {
        createUI();
        createMenu();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 500);
        setVisible(true);
    }

    public void createUI(){
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setName("TextArea");
        scrollPane.setName("ScrollPane");
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(20,20,240,240);

        add(scrollPane, BorderLayout.CENTER);
        setTitle("The first stage");
        ImageIcon saveIcon = new ImageIcon("resources/saveIcon.png");
        JButton save = new JButton(saveIcon);
        save.setName("SaveButton");
        Dimension buttonDim = new Dimension(35,35);
        save.setPreferredSize(buttonDim);

        ImageIcon loadIcon = new ImageIcon("resources/openIcon.png");
        JButton load = new JButton(loadIcon);
        load.setPreferredSize(buttonDim);
        load.setName("OpenButton");

        check.setName("UseRegExCheckbox");

        JButton search = new JButton(new ImageIcon("resources/search.png"));
        search.setName("StartSearchButton");
        search.setPreferredSize(buttonDim);
        search.addActionListener( e -> {
            startSearch();
        });

        JButton left = new JButton(new ImageIcon("resources/left.png"));
        left.setName("PreviousMatchButton");
        left.setPreferredSize(buttonDim);
        left.addActionListener(e -> {
            leftSearch();
        });

        JButton right = new JButton(new ImageIcon("resources/right.png"));
        right.setPreferredSize(buttonDim);
        right.setName("NextMatchButton");
        right.addActionListener(e -> {
            rightSearch();
        });

        load.addActionListener(e -> {
            loadFile();
        });
        save.addActionListener(e -> {
            saveFile();
        });
        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(125,25));

        fileChooser.setFileFilter(extensionFilter);
        fileChooser.setName("FileChooser");

        JPanel top = new JPanel();

        top.add(save);
        top.add(load);
        top.add(searchField);
        top.add(search);
        top.add(left);
        top.add(right);
        top.add(check);
        top.add(new JLabel("Use regex"));
        add(top, BorderLayout.NORTH);
    }

    public void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        menuBar.add(fileMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem loadMenu = new JMenuItem("Load");
        loadMenu.setName("MenuOpen");
        fileMenu.add(loadMenu);
        loadMenu.addActionListener( e -> {
            loadFile();
        });
        loadMenu.setMnemonic(KeyEvent.VK_L);

        JMenuItem saveMenu = new JMenuItem("Save");
        fileMenu.add(saveMenu);
        saveMenu.setName("MenuSave");
        saveMenu.addActionListener( e -> {
            saveFile();
        });
        saveMenu.setMnemonic(KeyEvent.VK_S);

        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);
        exit.setName("MenuExit");
        exit.addActionListener(e -> {
            this.dispose();
        });
        exit.setMnemonic(KeyEvent.VK_E);

        JMenu searchMenu = new JMenu("Search");
        menuBar.add(searchMenu);
        searchMenu.setName("MenuSearch");
        searchMenu.setMnemonic(KeyEvent.VK_S);

        JMenuItem startSearch = new JMenuItem("Start search");
        searchMenu.add(startSearch);
        startSearch.addActionListener(e -> {
            startSearch();
        });
        startSearch.setMnemonic(KeyEvent.VK_S);
        startSearch.setName("MenuStartSearch");

        JMenuItem prevSearch = new JMenuItem("Previous search");
        searchMenu.add(prevSearch);
        prevSearch.addActionListener(e -> {
            leftSearch();
        });
        prevSearch.setName("MenuPreviousMatch");
        prevSearch.setMnemonic(KeyEvent.VK_P);

        JMenuItem nextMatch = new JMenuItem("Next match");
        searchMenu.add(nextMatch);
        nextMatch.addActionListener(e -> {
            rightSearch();
        });
        nextMatch.setName("MenuNextMatch");
        nextMatch.setMnemonic(KeyEvent.VK_N);

        JMenuItem useRegex = new JMenuItem("Use regular expressions");
        searchMenu.add(useRegex);
        useRegex.addActionListener(e -> {
            if (!check.isSelected()) {
                check.doClick();
            }
        });
        useRegex.setName("MenuUseRegExp");
        useRegex.setMnemonic(KeyEvent.VK_R);
    }

    public void saveFile() {
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(textArea.getText());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    public void loadFile() {
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                if (file.isFile()) {
                    FileInputStream fileInput = new FileInputStream(file);
                    textArea.setText(new String(fileInput.readAllBytes(), StandardCharsets.UTF_8));
                } else {
                    textArea.setText(null);
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    public void startSearch() {
        String regex = searchField.getText();
        if (check.isSelected() && (regex.charAt(0)=='"' && regex.charAt(regex.length()-1)=='"')) {
            if (regex.charAt(1)=='\\' && regex.charAt(2)=='\\') {
                regex = regex.substring(2, regex.length()-1);
            } else {
                regex = regex.substring(1, regex.length() - 1);
            }
            System.out.println(regex);
        }
        Matcher matcher = Pattern.compile(regex).matcher(textArea.getText());
        if(!matcher.find()) {
            return;
        }
        int index = matcher.start();
        int indexLength = index + matcher.group().length();
        if (!(searchIndexes.isEmpty() && indexPlusLengths.isEmpty())) {
            searchIndexes = new ArrayDeque<>();
            indexPlusLengths = new ArrayDeque<>();
        }
        searchIndexes.addFirst(index);
        indexPlusLengths.addFirst(indexLength);
        textArea.setCaretPosition(index);
        textArea.select(index, indexLength);
        textArea.grabFocus();
    }

    public void rightSearch() {
        String regex = searchField.getText();
        if (check.isSelected() && (regex.charAt(0)=='"' && regex.charAt(regex.length()-1)=='"')) {
            if (regex.charAt(1)=='\\' && regex.charAt(2)=='\\') {
                regex = regex.substring(2, regex.length()-1);
            } else {
                regex = regex.substring(1, regex.length() - 1);
            }
        }
        Matcher matcher = Pattern.compile(regex).matcher(textArea.getText());
        if (!searchIndexes.isEmpty()) {
            matcher.find(searchIndexes.peekFirst() + 1);
        } else {
            if(!matcher.find()) {
                return;
            }
        }
        if (matcher.hitEnd()) {
            if(!matcher.find(0)) {
                return;
            }
        }
        int index = matcher.start();
        int indexLength = index + matcher.group().length();
        searchIndexes.addFirst(index);
        indexPlusLengths.addFirst(indexLength);
        textArea.setCaretPosition(index);
        textArea.select(index, indexLength);
        textArea.grabFocus();
    }

    public void leftSearch() {
        if ((searchIndexes.size() > 1 && indexPlusLengths.size() > 1)) {
            searchIndexes.removeFirst();
            indexPlusLengths.removeFirst();
            int index = searchIndexes.peekFirst();
            int indexLength = indexPlusLengths.peekFirst();
            textArea.setCaretPosition(index);
            textArea.select(index, indexLength);
            textArea.grabFocus();
        } else {
            addDeque();
        }
    }

    public void addDeque() {
        searchIndexes = new ArrayDeque<>();
        indexPlusLengths = new ArrayDeque<>();
        String regex = searchField.getText();
        if (check.isSelected() && (regex.charAt(0)=='"' && regex.charAt(regex.length()-1)=='"')) {
            if (regex.charAt(1)=='\\' && regex.charAt(2)=='\\') {
                regex = regex.substring(2, regex.length()-1);
            } else {
                regex = regex.substring(1, regex.length() - 1);
            }
        }
        Matcher matcher = Pattern.compile(regex).matcher(textArea.getText());
        int index = 0 ;
        int indexLength = 0;
        while (!matcher.hitEnd()) {
            matcher.find();
            if (matcher.hitEnd()){
                break;
            }
            index = matcher.start();
            indexLength = index + matcher.group().length();
            searchIndexes.addFirst(index);
            indexPlusLengths.addFirst(indexLength);
        }

        textArea.setCaretPosition(index);
        textArea.select(index, indexLength);
        textArea.grabFocus();
    }

}

