import java.util.List;
import java.awt.Point;
import java.util.Map;
import java.util.LinkedHashMap;

public class Paranoid extends SearchStrategy {
  
  private int depth_cut;
  private Heuristic strategy;
  private Map<State, Integer> actions;
  
  public Paranoid(Heuristic h, int depth) {
    this.strategy = h;
    this.depth_cut = depth;
    this.actions = new LinkedHashMap<State, Integer>(); 
  }
  
  public Point search(State state) {
    int result = paranoid(state, 0);
    Point action = pickBestAction(result);
    return action;
  }
  
  public int paranoid(State state, int depth) {
    if(terminal_test(state, depth)) {
      return strategy.evaluate(state);
    }
    int val = 0;
    if(depth % 4 == 0) { // Max
      val = Integer.MIN_VALUE;
      List<State> successors = state.expand();
      for(State nextState : successors) {
        int stateVal = paranoid(nextState, depth+1);
        val = Math.max(val, stateVal);
        if(depth == 0) {
          actions.put(nextState, stateVal);
        }
      }
    } else { // Min
      val = Integer.MAX_VALUE;
      List<State> successors = state.expand();
      for(State nextState : successors) {
        val = Math.min(val, paranoid(nextState, depth+1));
      }
    }
    return val;
  }
  
  // depth cut  
  private boolean terminal_test(State state, int depth) {
    if(depth == depth_cut || state.gameEnds()) {
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
    //System.out.println("Paranoid: " + action);
    actions.clear();
    return action;
  }
}