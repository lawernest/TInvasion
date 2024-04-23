import java.util.List;
import java.awt.Point;
import java.util.Random;

public class GameModel {
  
  private GameBoard board;
  public Team team1, team2;
  private Team[] order;
  public int passCount, curr;
  private Player player;
  
  public GameModel() {
    this.passCount = 0;
    this.board = new GameBoard();
    initTeam();
    this.board.initBoard(this.team1, this.team2);
  }
  
  public GameBoard getBoard() { return this.board; }
  public Team getCurrTeam() { return this.order[curr]; }
  public Team getTeam1() { return this.team1; }
  public Team getTeam2() { return this.team2; }
  public int getPassCount() { return this.passCount; }
  
  private void initTeam() {
    Random rand = new Random();
    this.player = new Player('R');
    // initialize team 1
    this.team1 = new Team(this.player, new Player('Y', new AlphaBeta(new Heuristic.CountDisk(), 6)));
    // initialize team 2
    this.team2 = new Team(new Player('G', new MonteCarlo(50)), 
                          new Player('B', new Paranoid(new Heuristic.CountDisk(), 4)));
    this.curr = rand.nextInt(2);
    this.order = new Team[] { this.team1, this.team2 };
  }
  
  public boolean isPlayerTurn() {
    //return this.order[curr].getCurrent().getAI() == null;
    return this.order[curr].getCurrent().equals(player);
  }
  
  public int gameEnds() {
    int[] teamDisks = this.board.countDisks(team1);
    int pScore = teamDisks[0] + teamDisks[1];
    int cScore = this.board.getOccupied() - pScore;
    if((this.board.isFull() || pScore == cScore) && passCount == getPlayablePlayers()) {
      return -1; // tie
    }
    if(this.board.isFull() || passCount == getPlayablePlayers() || pScore == 0 || cScore == 0) {
      return 1; // win
    }
    return 0; // continue
  }
  
  private int getPlayablePlayers() {
    int players = 0;
    for(int i = 0; i < this.order.length; i++) {
      if(this.order[i].getCurrent().isPlayable()) {
        players++;
      }
      if(this.order[i].getNext().isPlayable()) {
        players++;
      }
    }
    return players;
  }
  
  public Team getWinner() {
    int[] teamDisks = this.board.countDisks(team1);
    int pScore = teamDisks[0] + teamDisks[1];
    int cScore = this.board.getOccupied() - pScore;
    Team winner = (pScore > cScore) ? order[0] : order[1];
    return winner;
  }
  
  public List<Point> findValidMoves() {
    List<Point> moves = this.board.findValidMove(order[curr]);
    return moves;
  }
  
  public void pass() {
    this.passCount++;
  }
  
  public void resetCounter() {
    this.passCount = 0;
  }
  
  public void placeDisk(int row, int col) {
    this.board.placeDisk(row, col, this.order[curr]);
  }
  
  public void update() {
    List<Point> validMoves = findValidMoves();
    //double time = 0.0;
    if(validMoves.size() > 0) {
      //double start = System.currentTimeMillis();
      Point action = this.order[curr].getCurrent().getNextMove(this.board, this.order[curr], this.order[(curr+1)%2], this.passCount);
      //double end = System.currentTimeMillis();
      //time = (end - start)/1000;
      //System.out.println(time);
      placeDisk((int)action.getX(), (int)action.getY());
      resetCounter();
    } else {
      pass();
    }
    //return time;
  }
  
  public void nextTurn() {
    int[] teamDisks = this.board.countDisks(this.order[(curr+1)%2]);
    if(teamDisks[0] == 0) {
      this.order[(curr+1)%2].getCurrent().setPlayable(false);
    } 
    if(teamDisks[1] == 0) {
      this.order[(curr+1)%2].getNext().setPlayable(false);
    }
    this.order[curr].changeTurn();
    this.curr = (++this.curr) % order.length;
  }
}