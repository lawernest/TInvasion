public interface Heuristic {
  
  public int evaluate(State state);
  
  public class CountDisk implements Heuristic {
    public int evaluate(State state) {
      int[] teamDisks = state.getBoard().countDisks(state.getInitTeam());
      int initScore = teamDisks[0] + teamDisks[1];
      int cScore = state.getBoard().getOccupied() - initScore;
      int score = initScore - cScore;
      return score;
    }
  }
}