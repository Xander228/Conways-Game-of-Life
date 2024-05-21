import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
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

        JButton importFile = new GameButton("Select File"); //Creates a new button to reset the game
        //Add an actionListener object that runs actionPerformed when it senses the button press and restarts the game
        importFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton importString = new GameButton("Import"); //Creates a new button to exit the game
        //Add an actionListener object that runs actionPerformed when it senses the button press and exits the game
        importString.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

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

    public boolean[][] convertToArray(String s) throws FileNotFoundException {
        ArrayList<Boolean> pattern = new ArrayList<Boolean>();
        
        File dataFile = new File("glider.rle");

        Scanner scanner = new Scanner(dataFile);

        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.charAt(0) == '#') continue;
            if (line.charAt(0) == 'x') continue;
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter("$");
            int total = 0;
            int nScores = 0;
            System.out.print(lineScanner.next() + ": ");
            while (lineScanner.hasNext()) {
                total += lineScanner.nextInt();
                nScores++;
            }
            System.out.println(total /= nScores);
        }
    }

}
