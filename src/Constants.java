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

    public static final int HISTORY_CAPTURE_LENGTH = 200;


    public static final int IMPORT_PANEL_WIDTH = 600;
    public static final int IMPORT_PANEL_HEIGHT = 300;


    public static final Color BACKGROUND_COLOR = new Color((int)0x212121); //The color used as the background for most panels
    public static final  Color PRIMARY_COLOR = new Color((int)0x555555); //The color used to for headers and text for most panels
    public static final  Color ACCENT_COLOR = new Color((int)0x373737); //The color used to accent the headers and text for some panels
    public static final  Color X_COLOR = new Color((int)0x442121);
    public static final  Color Y_COLOR = new Color((int)0x214421);
    public static final  Color HOME_COLOR = new Color((int)0x212144);

    public static final  Color LIVE_COLOR = new Color((int)0xFFFFFF);
    public static final  Color X_LIVE_COLOR = new Color((int)0xFFAAAA);
    public static final  Color Y_LIVE_COLOR = new Color((int)0xAAFFAA);
    public static final  Color HOME_LIVE_COLOR = new Color((int)0xAAAAFF);

    public static final  Color HIGHLIGHT_COLOR = new Color((int)0x3FFFFF);
}


