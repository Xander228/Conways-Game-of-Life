import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainFrame extends JFrame {

    //Todo:
    //modify new board hash to store whether a cell has been calulated
    //or create new to do hash and write to it prior to calculations
    //Export
    //Import catalog



    public static MainFrame frame;
    public JPanel mainPanel;

    public MainFrame() {
        super();

        UIManager.put("ToolTip.foreground", Constants.BACKGROUND_COLOR);
        UIManager.put("ToolTip.background", Constants.PRIMARY_COLOR);
        UIManager.put("ToolTip.border",BorderFactory.createMatteBorder(1,1,1,1,Constants.BACKGROUND_COLOR));


        setTitle("Conway's Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(10,10,10,10,Constants.ACCENT_COLOR)); //Add a border around the frame
        mainPanel.setBackground(Constants.ACCENT_COLOR); //Set the background color of the panel
        mainPanel.setLayout(new BorderLayout(10,10)); //Sets the edge offset of member panels to properly space them

        ButtonPanel buttonPanel = new ButtonPanel(this);
        TopButtonPanel topButtonPanel = new TopButtonPanel(this);
        GamePanel gamePanel = new GamePanel();

        mainPanel.add(topButtonPanel,BorderLayout.NORTH);
        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setFocusable(true);

        //Set the frame visible
        setVisible(true);
    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                frame = new MainFrame();
            }
        });
    }



}