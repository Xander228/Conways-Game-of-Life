public class GameHistory {

    private int cursorPosition;
    private GameState[] previousStates;

    GameHistory(){
        previousStates = new GameState[Constants.HISTORY_CAPTURE_LENGTH];
        cursorPosition = 0;
    }

    public void addToHistory(GameState gameState){
        for(int i = 0; i < previousStates.length; i++){
            try {
                previousStates[i] = previousStates[cursorPosition + i];
            }
            catch (ArrayIndexOutOfBoundsException e){
                previousStates[i] = null;
            }

        }
        cursorPosition = 0;

        for (int i = previousStates.length - 1; i > 0; i--){
            previousStates[i] = previousStates[i - 1];
        }
        previousStates[0] = gameState;
    }

    public GameState undo(){
        try{
            if (previousStates[cursorPosition + 1] != null)  cursorPosition++;
            return previousStates[cursorPosition];
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }

    }

    public GameState redo(){
        try{
            if (previousStates[cursorPosition - 1] != null)  cursorPosition--;
            return previousStates[cursorPosition];
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }

    }
}
