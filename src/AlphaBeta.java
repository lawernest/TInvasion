import java.util.List;
import java.awt.Point;
import java.util.Map;
import java.util.LinkedHashMap;

public class AlphaBeta extends SearchStrategy {
  
  private int depth_cut;
  private Heuristic strategy;
  private Map<State, Integer> actions;
  private int ALPHA = Integer.MIN_VALUE;
  private int BETA = Integer.MAX_VALUE;
  
  public AlphaBeta(Heuristic h, int depth) {
    this.strategy = h;
    this.depth_cut = depth;
    this.actions = new LinkedHashMap<State, Integer>();
  }
  
  public Point search(State state) {
    int result = max_value(state, ALPHA, BETA, depth_cut);
    Point action = pickBestAction(result);
    return action;
  }

  private int max_value(State state, int alpha, int beta, int depth) {
    if(terminal_test(state, depth)) {
      return strategy.evaluate(state);
    }
    
    List<State> successors = state.expand();
    int v = Integer.MIN_VALUE;
    for(State nextState : successors) {
      int stateV = min_value(nextState, alpha, beta, depth - 1);
      v = Math.max(v, stateV);
      if(depth == depth_cut) {
        this.actions.put(nextState, stateV);
      }
      if(v >= beta) {
        return v;
      } 
      alpha = Math.max(alpha, v);
    }
    return v;
  }
  
  private int min_value(State state, int alpha, int beta, int depth) {
    if(terminal_test(state, depth)) {
      return strategy.evaluate(state);
    }
    
    List<State> successors = state.expand();
    int v = Integer.MAX_VALUE;
    for(State nextState : successors) {
      v = Math.min(v, max_value(nextState, alpha, beta, depth - 1));
      if(v <= alpha) {
        return v;
      }
      beta = Math.min(beta, v);
    }
    return v;
  }
  
  private boolean terminal_test(State state, int depth) {
    if(depth == 0 || state.gameEnds()) {
      return true;
    }
    return false;
  }
  
  private Point pickBestAction(int bestMove) {
    Point action = null;
    for(Map.Entry<State, Integer> entry : actions.entrySet()) {
      if(entry.getValue() == bestMove) {
        action = entry.getKey().getAction();
        break;
      }
    }
    //System.out.println("Alpha Beta: " + action);
    actions.clear();
    return action;
  }
}