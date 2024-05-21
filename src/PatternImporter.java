import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class PatternImporter extends JDialog {

    PatternImporter() {
        super((Frame) null, "Import String"); //Call the parent class's constructor
        this.setSize(Constants.IMPORT_WIDTH, Constants.IMPORT_HEIGHT); //Sets the size of the dialog
        this.setLocationRelativeTo(MainFrame.frame);
        JPanel dialogPanel = new JPanel(); //Declare and initialize the dialogPanel that stores the message, buttons, and scores
        dialogPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Constants.BACKGROUND_COLOR)); //Add a border around the frame
        dialogPanel.setLayout(new BorderLayout(0, 0)); //Sets the edge offset of member panels to properly space them

        JPanel textPanel = new JPanel(); //Declare and initialize the textPanel that stores the game over message
        textPanel.setBackground(Constants.PRIMARY_COLOR); //Sets the background color of the textPanel
        JLabel text = new JLabel("Paste string below or select a file", JLabel.CENTER); //Creates a new label object with the text "GAME OVER" to label the panel
        text.setFont(new Font("Arial", Font.BOLD, 20)); //Sets the font and size of the label
        text.setForeground(Constants.BACKGROUND_COLOR); //Sets the color of the text
        textPanel.add(text, BorderLayout.NORTH); //Adds the text object to the top of textPanel

        JPanel inputPanel = new JPanel(); //Declare and initialize the inputPanel that stores the scores
        inputPanel.setBorder(BorderFactory.createMatteBorder(10, 0, 10, 0, Constants.BACKGROUND_COLOR)); //Add a border around the frame
        inputPanel.setLayout(new BorderLayout(0, 0)); //Sets the edge offset of member panels to properly space them
        inputPanel.setBackground(Constants.BACKGROUND_COLOR); //Sets the background color of the inputPanel

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAutoscrolls(true);

        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Constants.PRIMARY_COLOR;
            }
        });

        inputPanel.add(areaScrollPane);

        JPanel buttonPanel = new JPanel(); //Declare and initialize the buttonPanel that stores the buttons
        buttonPanel.setBorder(BorderFactory.createMatteBorder(5, 50, 5, 50, Constants.BACKGROUND_COLOR)); //Add a border around the frame
        buttonPanel.setLayout(new BorderLayout(0, 0)); //Sets the edge offset of member panels to properly space them
        buttonPanel.setBackground(Constants.BACKGROUND_COLOR); //Sets the background color of the textPanel

        //Create a subclass of JButton made to display the action buttons
        class GameButton extends JButton {
            GameButton(String text) {
                super(text); //Call the parent class's constructor
                this.setFocusable(false); //Set focusable to false in order to remove the visible focus outline
                this.setPreferredSize(new Dimension(120, 45)); //Sets the size of the button
                this.setBackground(Constants.PRIMARY_COLOR);  //Sets the background color
                this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constants.ACCENT_COLOR)); //Add a border around the button
                this.setFont(new Font("Arial", Font.BOLD, 16)); //Sets the font and size of the label in the button
                this.setForeground(Constants.BACKGROUND_COLOR); //Sets the color of the text
            }
        }

        String userhome = System.getProperty("user.home");
        JFileChooser fileChooser = new JFileChooser(userhome +"\\Downloads");
        fileChooser.setDialogTitle("Choose a pattern file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Pattern Files", "rle", "txt"));

        JButton importFile = new GameButton("Select File"); //Creates a new button to reset the game
        //Add an actionListener object that runs actionPerformed when it senses the button press and restarts the game
        importFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.updateUI();
                fileChooser.showOpenDialog(null);
                try {
                    textArea.setText(Files.readString(Paths.get(fileChooser.getSelectedFile().toURI())));
                } catch (Exception ae){

                }
            }
        });

        JButton importString = new GameButton("Import"); //Creates a new button to exit the game
        //Add an actionListener object that runs actionPerformed when it senses the button press and exits the game
        importString.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    GamePanel.patternPlacer = new PatternPlacer(convertToArray(textArea.getText()));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        buttonPanel.add(importFile, BorderLayout.WEST); //Adds the restart button object to the left of the buttonPanel
        buttonPanel.add(importString, BorderLayout.EAST); //Adds the exit button object to the right of the buttonPanel

        dialogPanel.add(textPanel, BorderLayout.NORTH); //Adds the textPanel object to the top of the dialogPanel
        dialogPanel.add(inputPanel, BorderLayout.CENTER); //Adds the inputPanel object to the center of the dialogPanel
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH); //Adds the buttonPanel object to the bottom of the dialogPanel
        this.add(dialogPanel); //Adds the dialogPanel to the dialog
        this.setVisible(true); //Sets the dialog to visible
    }

    public boolean[][] convertToArray(String string) throws FileNotFoundException {
        String patternCode = "";

        Scanner scanner = new Scanner(string);
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.charAt(0) == '#') continue;
            if (line.charAt(0) == 'x') continue;
            patternCode += line;
        }

        Scanner patternScanner = new Scanner(patternCode);
        patternScanner.useDelimiter("[\\$!]");
        int xSize = 0;
        int ySize = 0;
        ArrayList<ArrayList<Boolean>> pattern = new ArrayList<ArrayList<Boolean>>();

        while (patternScanner.hasNext()) {
            ArrayList<Boolean> patternLine = new ArrayList<Boolean>();
            String line = patternScanner.next();
            int lastCellIndex = -1;
            for (int i = 0; i < line.length();i++){
                int repeat;
                if(!(line.charAt(i) == 'b' || line.charAt(i) == 'o')) continue;
                try {
                    repeat = Integer.parseInt(line.substring(lastCellIndex + 1, i));
                }
                catch (Exception e) {
                    repeat = 1;
                }
                lastCellIndex = i;
                boolean alive = line.charAt(i) == 'o';
                for(int j = 0; j < repeat; j++) patternLine.add(alive);
            }
            xSize = Math.max(xSize, patternLine.size());
            pattern.add(patternLine);
            ySize++;
        }

        boolean[][] output = new boolean[xSize][ySize];
        for (int y = 0; y < ySize; y++){
            ArrayList<Boolean> patternLine = pattern.get(y);
            for (int x = 0; x < xSize; x++){
                try {
                    output[x][y] = patternLine.get(x);
                }
                catch(IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return output;
    }

}
