import java.awt.*;

public class Constants {



    public static final int DISPLAY_LOOPS_PER_SECOND = 60;
    public static final int DISPLAY_LOOP_TIME = 1000 / DISPLAY_LOOPS_PER_SECOND;

    public static final int DEFAULT_GAME_DELAY = 100;



    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;
    
    public static final int DESIRED_VIEWPORT_WIDTH = 1000;
    public static final int DESIRED_VIEWPORT_HEIGHT = 500;


    public static final int BUTTON_HEIGHT = 100;

    public static final int DEFAULT_CELL_WIDTH = 16;
    public static final int MAX_CELL_WIDTH = 60;
    public static final int MIN_CELL_WIDTH = 1;
    public static final double CELL_BORDER_RATIO = 0.1;
    public static final double ZOOM_SCALE_FACTOR = 0.1;


    public static final int IMPORT_WIDTH = 600;
    public static final int IMPORT_HEIGHT = 300;

    

    public static enum PanelStates {
        RUN_PHASE (),
        IDLE_PHASE ();

        PanelStates(){}
    }


    public static final Color BACKGROUND_COLOR = new Color((int)0x212121); //The color used as the background for most panels
    public static final  Color PRIMARY_COLOR = new Color((int)0x555555); //The color used to for headers and text for most panels
    public static final  Color ACCENT_COLOR = new Color((int)0x373737); //The color used to accent the headers and text for some panels
    public static final  Color LIVE_COLOR = new Color((int)0xFFFFFF);
    public static final  Color HIGHLIGHT_COLOR = new Color((int)0x3FFFFF);
}


