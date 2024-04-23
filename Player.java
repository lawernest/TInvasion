import java.awt.Point;

public class Player {
  
  private char disk_color;
  private SearchStrategy ai;
  private boolean playable;
  
  public Player(char color) {
    this.disk_color = color;
    this.ai = null;
    this.playable = true;
  }
  
  public Player(char color, SearchStrategy search) {
    this(color);
    this.ai = search;
  }
  
  public char getDisk() { return this.disk_color; }
  public SearchStrategy getAI() { return ai; }
  public boolean isPlayable() { return this.playable; }
  
  public void setPlayable(boolean p) {
    this.playable = p;
  }
  
  public Point getNextMove(GameBoard board, Team curr, Team next, int counter) {
    State state = new State(board, curr, next, curr, counter);
    return ai.search(state);
  }
  
  @Override
  public boolean equals(Object o) {
    if(o == this) {
      return true;
    }
    if(!(o instanceof Player)) {
      return false;
    }
    Player p = (Player) o;
    return this.disk_color == p.disk_color;
  }
}