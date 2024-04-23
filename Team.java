import java.util.Random;

public class Team implements Cloneable {
  
  private static int id = 1;
  private Player currP, nextP;
  private int teamNum;
  
  public Team(Player curr, Player next) {
    Random rand = new Random();
    int r = rand.nextInt(2);
    if(r == 0) {
      this.currP = curr;
      this.nextP = next;
    } else {
      this.currP = next;
      this.nextP = curr;
    }
    this.teamNum = id++;
  }
  
  public Player getCurrent() { return this.currP; }
  public Player getNext() { return this.nextP; }
  public int getTeamNum() { return this.teamNum; }
  
  public void changeTurn() {
    if(this.nextP.isPlayable()) {
      Player p = this.currP;
      this.currP = this.nextP;
      this.nextP = p;
    }
  }
  
  @Override
  protected Team clone() {
    Team clone = null;
    try {
      clone = (Team) super.clone();    
      clone.currP = new Player(this.currP.getDisk());
      clone.nextP = new Player(this.nextP.getDisk());
      clone.teamNum = this.teamNum;
    } catch (CloneNotSupportedException e) {
      System.out.println("Cloning Error");
    }
    return clone;
  }
  
  @Override
  public boolean equals(Object o) {
    if(o == this) {
      return true;
    }
    if(!(o instanceof Team)) {
      return false;
    }
    Team t = (Team) o;
    return this.teamNum == t.teamNum;
  }
}