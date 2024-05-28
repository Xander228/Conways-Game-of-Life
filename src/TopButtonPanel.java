import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TopButtonPanel extends JPanel {


    private static JLabel generationLabel;
    private static JLabel boardSizeLabel;
    private static JLabel cursorPositionLabel;
    private static JLabel cellCountLabel;
    private static JLabel fpsLabel;
    private static JLabel tpsLabel;

    TopButtonPanel(MainFrame frame){
        super();
        setLayout(new BorderLayout(5, 5));
        setBackground(Constants.BACKGROUND_COLOR);

        class GameButton extends JButton {
            Image image;

            GameButton(String imageFilename) {
                try {
                    image = ImageIO.read(this.getClass().getResource(imageFilename));
                } catch (Exception e) {
                    this.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
                }

                this.setFocusable(false);
                this.setPreferredSize(new Dimension(32, 32));
                this.setBackground(Constants.PRIMARY_COLOR);
                this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Constants.ACCENT_COLOR));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                if (image != null) g2.drawImage(image, 1, 0, 30, 30, this);
            }
        }

        JButton home = new GameButton("home.png");
        home.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                GamePanel.gamePanel.setViewPortHome();


            }
        });


        JButton undo = new GameButton("undo.png");
        undo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                BoardManager.stopTimer();
                GameState newState = GamePanel.gameHistory.undo();
                if (newState == null) return;
                GamePanel.generation = newState.getGeneration();
                BoardManager.board = newState.getBoardState();

            }
        });

        JButton redo = new GameButton("redo.png");
        redo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                BoardManager.stopTimer();
                GameState newState = GamePanel.gameHistory.redo();
                if (newState == null) return;
                GamePanel.generation = newState.getGeneration();
                BoardManager.board = newState.getBoardState();
            }
        });

        class GameLabel extends JLabel {
            GameLabel(String text){
                super(text);
                this.setFocusable(false);
                this.setFont(new Font("Arial", Font.BOLD, 12));
                this.setForeground(Constants.HIGHLIGHT_COLOR);
            }
        }

        JPanel centerSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
        centerSubPanel.setBackground(Constants.BACKGROUND_COLOR);

        centerSubPanel.add(generationLabel = new GameLabel("Generation:"));
        centerSubPanel.add(boardSizeLabel = new GameLabel("Active Board Size: w: h:"));
        centerSubPanel.add(cursorPositionLabel = new GameLabel("Cursor Position: x: y:"));
        centerSubPanel.add(cellCountLabel = new GameLabel("Live Cells:"));
        centerSubPanel.add(fpsLabel = new GameLabel("FPS:"));
        centerSubPanel.add(tpsLabel = new GameLabel("TPS:"));


        JPanel eastSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        eastSubPanel.setBackground(Constants.BACKGROUND_COLOR);

        eastSubPanel.add(undo);
        eastSubPanel.add(redo);


        JPanel westSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        westSubPanel.setBackground(Constants.BACKGROUND_COLOR);

        westSubPanel.add(home);




        add(westSubPanel,BorderLayout.WEST);
        add(centerSubPanel,BorderLayout.CENTER);
        add(eastSubPanel,BorderLayout.EAST);


    }

    public static void updateLabels(){
        generationLabel.setText("Generation: " + GamePanel.generation);
        boardSizeLabel.setText("Active Board Size: w:" +
                 (BoardManager.board.getXMax() - BoardManager.board.getXMin()) +
                 " h:" + (BoardManager.board.getYMax() - BoardManager.board.getYMin()));
        Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, GamePanel.gamePanel);
        cursorPositionLabel.setText("Cursor Position: x:" +
                (int)Math.floor(p.getX() / GamePanel.cellWidth - GamePanel.viewPortOffsetX - GamePanel.liveViewPortOffsetX) +
                " y:" + -(int)Math.floor(p.getY() / GamePanel.cellWidth - GamePanel.viewPortOffsetY - GamePanel.liveViewPortOffsetY));
        cellCountLabel.setText("Live Cells: " + BoardManager.board.getSize());
        fpsLabel.setText("FPS: " + GamePanel.fps);
        tpsLabel.setText("TPS: " + BoardManager.tps);

    }
}



