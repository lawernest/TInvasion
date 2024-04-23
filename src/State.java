import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class State {
  
  private GameBoard board;
  private Team curr, next, initTeam;
  private Point action;
  private int pass_counter;
  
  public State(GameBoard b, Team c, Team n, Team init, int counter) {
    this.board = b.clone();
    this.curr = c.clone();
    this.next = n.clone();
    this.initTeam = init.clone();
    this.action = null;
    this.pass_counter = counter;
  }
  
  public State(State s) {
    this.board = s.board.clone();
    this.curr = s.curr.clone();
    this.next = s.next.clone();
    this.initTeam = s.initTeam.clone();
    this.action = s.action;
    this.pass_counter = s.pass_counter;
  }
  
  public int getNumPass() { return this.pass_counter; }
  public Team getInitTeam() { return this.initTeam; }
  public GameBoard getBoard() { return this.board; }
  public Team getCurrTeam() { return this.curr; }
  public Team getNextTeam() { return this.next; }
  public Point getAction() { return this.action; }
  
  public Team getAnotherTeam() {
    if(this.initTeam.equals(this.curr)) {
      return this.next;
    }
    return this.curr;
  }
  
  public void pass() {
    this.pass_counter++;
  }
  
  private int getPlayablePlayers() {
    int players = 0;
    if(curr.getCurrent().isPlayable()) {
      players++;
    }
    if(curr.getNext().isPlayable()) {
      players++;
    }
    if(next.getCurrent().isPlayable()) {
      players++;
    }
    if(next.getNext().isPlayable()) {
      players++;
    }
    return players;
  }
  
  public boolean gameEnds() {
    int[] teamDisks = this.board.countDisks(initTeam);
    int pScore = teamDisks[0] + teamDisks[1];
    int cScore = this.board.getOccupied() - pScore;
    if((this.board.isFull() || pScore == cScore) && pass_counter == getPlayablePlayers()) {
      return true; // tie
    }
    if(this.board.isFull() || pass_counter == 4 || pScore == 0 || cScore == 0) { // board is full or each player has passed once
      return true; // win
    }
    return false;
  }
  
  // For Monte Carlo
  public void playRandomMove() {
    List<Point> moves = this.board.findValidMove(this.curr);
    if(moves.size() == 0) {
      pass();
      return;
    }
    int r = (int)(Math.random() * moves.size());
    Point nextMove = moves.get(r); 
    this.board.placeDisk((int)nextMove.getX(), (int)nextMove.getY(), this.curr);
  }
  
  public void switchTeam() {
    Team temp = this.curr;
    this.curr = this.next;
    this.next = temp;
  }
  
  public Team getWinner() {
    int[] teamDisks = this.getBoard().countDisks(this.initTeam);
    int pScore = teamDisks[0] + teamDisks[1];
    int cScore = this.getBoard().getOccupied() - pScore;
    Team winner = null;
    if(pScore > cScore) {
      winner = this.initTeam;
    } else if(cScore > pScore) {
      winner = getAnotherTeam();
    }    
    return winner;
  }
  
  public List<State> expand() {
    List<State> successors = new ArrayList<State>();
    List<Point> moves = this.board.findValidMove(this.curr);
    
    if(moves.size() == 0) { //pass
      State nextState = new State(this.board, this.next, this.curr, this.initTeam, this.pass_counter); // create the next state
      nextState.pass();
      nextState.next.changeTurn();
      successors.add(nextState);
    } else {
      for(Point move : moves) {
        State nextState = new State(this.board, this.next, this.curr, this.initTeam, this.pass_counter); // create the next state
        nextState.board.placeDisk((int)move.getX(), (int)move.getY(), this.curr); // place the disk
        nextState.action = new Point(move); // score the move
        int[] teamDisks = nextState.board.countDisks(nextState.curr);
        if(teamDisks[0] == 0) {
          nextState.curr.getCurrent().setPlayable(false);
        } 
        if(teamDisks[1] == 0) {
          nextState.curr.getNext().setPlayable(false);
        }
        nextState.next.changeTurn(); // change turn
        successors.add(nextState);
      }
    }
    return successors;
  }
}