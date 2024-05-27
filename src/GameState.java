public class GameState {
    int generation;
    ManagedBoard boardState;

    GameState(int generation, ManagedBoard boardState){
        this.generation = generation;
        this.boardState = boardState.copy();
    }

    public int getGeneration() {
        return generation;
    }

    public ManagedBoard getBoardState() {
        return boardState;
    }
}