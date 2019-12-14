import java.util.ArrayList;
import java.util.List;



public class Agent {
    /**
     * current game state
     */
    private GameState _state;

    public AI(GameState state) {
        this._state = state;
    }
    

    /**
     * @return state.features * state.weight
     * 
     * @explain We need a score to do the minimax tree. However, simply just use the game score wouldn't be good.
     */
    private double evaluate() {
        double smoothWeight = 0.2;
        double monoWeight = 1.3;
        double emptyWeight = 2.0; 
        double maxWeight = 0.7;
        return _state.smoothness() * smoothWeight
                + _state.monotonicity() * monoWeight
                + _state.emptiness() * emptyWeight
                + _state.maxValue() * maxWeight;
    }



}