public class GameState {
    int generation;
    DynamicBoard boardState;

    GameState(int generation, DynamicBoard boardState){
        this.generation = generation;
        this.boardState = boardState.copy();
    }

    public int getGeneration() {
        return generation;
    }

    public DynamicBoard getBoardState() {
        return boardState;
    }
}