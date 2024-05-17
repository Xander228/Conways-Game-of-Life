import java.awt.*;

public class Constants {

    public static final int DISPLAY_LOOPS_PER_SECOND = 60;
    public static final int DISPLAY_LOOP_TIME = 1000 / DISPLAY_LOOPS_PER_SECOND;

    public static final int DEFAULT_GAME_DELAY = 100;


    public static final double CELL_WIDTH = 4;
    public static final int BOARD_WIDTH = 200;
    public static final int BOARD_HEIGHT = 200;
    public static final int BOARD_PIXEL_WIDTH = (int)(BOARD_WIDTH * CELL_WIDTH); //The visible width of the board, in pixels
    public static final int BOARD_PIXEL_HEIGHT = (int)(BOARD_HEIGHT * CELL_WIDTH); //The visible width of the board, in pixels
    public static final double CELL_BORDER_WIDTH = 1;
    public static final int BUTTON_HEIGHT = 100; //The visible width of the board, in pixels


    

    public static enum PanelStates {
        RUN_PHASE (),
        IDLE_PHASE ();

        PanelStates(){}
    }


    public static final Color BACKGROUND_COLOR = new Color((int)0x212121); //The color used as the background for most panels
    public static final  Color PRIMARY_COLOR = new Color((int)0x555555); //The color used to for headers and text for most panels
    public static final  Color ACCENT_COLOR = new Color((int)0x373737); //The color used to accent the headers and text for some panels
    public static final  Color LIVE_COLOR = new Color((int)0xFFFFFF);
}


