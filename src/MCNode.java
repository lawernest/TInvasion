import java.util.List;
import java.util.ArrayList;

public class MCNode {
  
  private MCNode parent;
  private State state;
  private int visited;
  private double numWin;
  private List<MCNode> children;
  
  public MCNode(State s) {
    this.parent = null;
    this.state = s;
    this.visited = 0;
    this.numWin = 0.0;
    this.children = new ArrayList<MCNode>();
  }
  
  public MCNode(State s, MCNode p) {
    this(s);
    this.parent = p;
  }
  
  public State getState() { return this.state; }
  public MCNode getParent() { return this.parent; }
  public List<MCNode> getChildren() { return this.children; }
  public int getNumVisited() { return this.visited; }
  public double getNumWin() { return this.numWin; }
  
  public void addChild(MCNode child) {
    this.children.add(child);
  }
  
  public void increaseNumVisited() {
    this.visited++;
  }
  
  public void increaseNumWinForDraw() {
    this.numWin += 0.5;
  }
  
  public void increaseNumWin() {
    this.numWin += 1.0;
  }
  
  public MCNode getRandomChild() {
    int childrenSize = this.children.size();
    int selectChild = (int)(Math.random() * childrenSize);
    return this.children.get(selectChild);
  }
  
  public MCNode getChildWithHighestUCT() {
    MCNode selected = this.children.get(0);
    double highest = 0.0;
    for(MCNode node : this.children) {
      double winRate = (visited > 0) ? node.numWin/node.visited : 0.0;
      if(winRate > highest) {
        highest = winRate;
        selected = node;
      }
    }
    return selected;
  }
}