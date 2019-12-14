import java.util.Random;


public class GameState{
    public static int score;
    public static Random random = new Random();
    private boolean _win;
    private boolean _lose;
    
    public boolean isWin() {
        return this._win;
    }

    public boolean isLose() {
        return this._lose;
    }

    /**
     * Check to see whether the game is ending
     * @param currState
     * @param thisGame
     */
    public void process(GameState currState, Game thisGame) {
        if (currState.isWin()) {
            this.win(currState, thisGame);
        }
        if (currState.isLose()) {
            this.lose(currState, thisGame);
        }
    }

    public GameState generateSuccessor(GameState now, Actions direction) {
        if (self.isWin() || self.isLose()) {
            throw Exception("Can't generate a successor of a terminal state.");
        }
    }

    public double smoothness() {
        return 0.0;
    }

    public double monotonicity() {
        return 0.0;
    }

    public double maxValue() {
        return 0.0;
    }

    public double emptiness() {
        return 0.0;
    }
}