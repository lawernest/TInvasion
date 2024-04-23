import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class GameBoard implements Cloneable {
  
  private char[][] board;
  private int occupied;
  public final static int SIZE = 10; // 10
  
  public GameBoard() {
    this.board = new char[SIZE][SIZE];
    this.occupied = 0;
    for(int i = 0; i < SIZE; i++)
      Arrays.fill(this.board[i], ' ');
  }
  
  public int getOccupied() { return this.occupied; }
  public boolean isFull() { return this.occupied == SIZE*SIZE; }
  public char[][] getBoard() { return this.board; }
    
  private boolean isGridEmpty(int row, int col) {
    return this.board[row][col] == ' ';
  }
  
  private boolean isValidPos(int pos) {
    return (pos >= 0 && pos < SIZE);
  }
  
  public int[] countDisks(Team team) {
    int[] counters = new int[2];
    for(int i = 0; i < SIZE; i++) {
      for(int j = 0; j < SIZE; j++) {
        char currDisk = team.getCurrent().getDisk();
        char nextDisk = team.getNext().getDisk();
        if(this.board[i][j] == currDisk) {
          counters[0]++;
        } else if (this.board[i][j] == nextDisk){
          counters[1]++;
        }
      }
    }
    return counters;
  }
  
  public List<Point> findValidMove(Team curr) {
    List<Point> moves = new ArrayList<Point>();
    for(int i = 0; i < SIZE; i++) {
      for(int j = 0; j < SIZE; j++) {
        if(validMove(i, j, curr)) {
          moves.add(new Point(i, j));
        }
      }
    }
    return moves;
  }
    
  public void placeDisk(int row, int col, Team team) {
    this.board[row][col] = team.getCurrent().getDisk();
    this.occupied++;
    flipDisk(row, col, team);
  }
  
  private void flipHelper(int row, int col, int mr, int mc, int end, char disk) {
    if(end > 0) {
      for(int i = 1; i <= end; i++) {
        this.board[row + i * mr][col + i * mc] = disk;
      }
    }
  }
  
  private void flipDisk(int row, int col, Team team) {
    char playerDisk = team.getCurrent().getDisk();
    
    int end = findDiskInLH(row, col, team); // Left
    flipHelper(row, col, 0, -1, end, playerDisk);  
    end = findDiskInRH(row, col, team); // Right 
    flipHelper(row, col, 0, 1, end, playerDisk);
    end = findDiskInUV(row, col, team); // Up
    flipHelper(row, col, -1, 0, end, playerDisk);
    end = findDiskInDV(row, col, team); // Down
    flipHelper(row, col, 1, 0, end, playerDisk);
    end = findDiskInLDD(row, col, team); // Left Down
    flipHelper(row, col, 1, -1, end, playerDisk);
    end = findDiskInLUD(row, col, team); // Left UP
    flipHelper(row, col, -1, 1, end, playerDisk);
    end = findDiskInRDD(row, col, team); // Right down
    flipHelper(row, col, 1, 1, end, playerDisk);
    end = findDiskInRUD(row, col, team); // Right up
    flipHelper(row, col, -1, -1, end, playerDisk);
  }
  
  private boolean validMove(int row, int col, Team team) {
    if(!isValidPos(row) || !isValidPos(col) || !isGridEmpty(row, col)) {
      return false;
    }
    int   leftH = findDiskInLH(row, col, team); // Horizontal  
    int  rightH = findDiskInRH(row, col, team);
    int     upV = findDiskInUV(row, col, team);  // Vertical
    int   downV = findDiskInDV(row, col, team);
    int  leftDD = findDiskInLDD(row, col, team); // Left Diagonal
    int  leftUD = findDiskInLUD(row, col, team);
    int rightDD = findDiskInRDD(row, col, team); // Right Diagonal
    int rightUD = findDiskInRUD(row, col, team);
    
    if(leftH == -1 || rightH == -1 || upV == -1 || downV == -1 || leftDD == -1|| leftUD == -1 || rightDD == -1 || rightUD == -1) {
      return false;
    }
    if(leftH > 0 || rightH > 0 || upV > 0 || downV > 0 || leftDD > 0 || leftUD > 0 || rightDD > 0 || rightUD > 0) {
      return true;
    }    
    return false;
  }
  
  // Left Horizontal
  private int findDiskInLH(int row, int col, Team team) {
    int counter = 0;
    boolean invalid = false;
    while(--col >= 0) {
      if(isGridEmpty(row, col)) {
        return -2;
      }
      if(this.board[row][col] == team.getNext().getDisk()) { // find teammate's disk
        invalid = true; // invalid
      }
      if(this.board[row][col] == team.getCurrent().getDisk()) { // find your disk;
        return !invalid ? counter : -1;
      }
      counter++;
    }
    return -2; // no disk is found case
  }
  
  // check for Right horizontal
  private int findDiskInRH(int row, int col, Team team) {    
    int counter = 0;
    boolean invalid = false;
    while(++col < SIZE) {
      if(isGridEmpty(row, col)) {
        return -2;
      }
      if(this.board[row][col] == team.getNext().getDisk()) { // find teammate's disk
        invalid = true; // invalid
      }
      if(this.board[row][col] == team.getCurrent().getDisk()) { // find your disk;
        return !invalid ? counter : -1;
      }
      counter++;
    }
    return -2;
  }
 
  // Check for Upward vertical
  private int findDiskInUV(int row, int col, Team team) {
    int counter = 0;
    boolean invalid = false;
    while(--row >= 0) {
      if(isGridEmpty(row, col)) {
        return -2;
      }
      if(this.board[row][col] == team.getNext().getDisk()) { // find teammate's disk
        invalid = true; // invalid
      }
      if(this.board[row][col] == team.getCurrent().getDisk()) { // find your disk;
        return !invalid ? counter : -1;
      }
      counter++;
    }
    return -2;
  }
  
  // Check for Downward vertical
  private int findDiskInDV(int row, int col, Team team) {
    int counter = 0;
    boolean invalid = false;
    while(++row < SIZE) {
      if(isGridEmpty(row, col)) {
        return -2;
      }
      if(this.board[row][col] == team.getNext().getDisk()) { // find teammate's disk
        invalid = true; // invalid
      }
      if(this.board[row][col] == team.getCurrent().getDisk()) { // find your disk;
        return !invalid ? counter : -1;
      }
      counter++;
    }
    return -2;
  }
  
  // Check for Left Downward Diagonal
  private int findDiskInLDD(int row, int col, Team team) {
    int counter = 0;
    boolean invalid = false;
    while(--col >= 0 && ++row < SIZE) {
      if(isGridEmpty(row, col)) {
        return -2;
      }
      if(this.board[row][col] == team.getNext().getDisk()) { // find teammate's disk
        invalid = true; // invalid
      }
      if(this.board[row][col] == team.getCurrent().getDisk()) { // find your disk;
        return !invalid ? counter : -1;
      }
      counter++;
    }
    return -2;
  }
  
  // Check for Left Up Diagonal
  private int findDiskInLUD(int row, int col, Team team) {
    int counter = 0;
    boolean invalid = false;
    while(++col < SIZE && --row >= 0) {
      if(isGridEmpty(row, col)) {
        return -2;
      }
      if(this.board[row][col] == team.getNext().getDisk()) { // find teammate's disk
        invalid = true; // invalid
      }
      if(this.board[row][col] == team.getCurrent().getDisk()) { // find your disk;
        return !invalid ? counter : -1;
      }
      counter++;
    }
    return -2;
  }
  
  // Check for Right Downward Diagonal
  private int findDiskInRDD(int row, int col, Team team) {
    int counter = 0;
    boolean invalid = false;
    while(++col < SIZE && ++row < SIZE) {
      if(isGridEmpty(row, col)) {
        return -2;
      }
      if(this.board[row][col] == team.getNext().getDisk()) { // find teammate's disk
        invalid = true; // invalid
      }
      if(this.board[row][col] == team.getCurrent().getDisk()) { // find your disk;
        return !invalid ? counter : -1;
      }
      counter++;
    }
    return -2;
  }
  
  // Check for Right Upward Diagonal
  private int findDiskInRUD(int row, int col, Team team) {
    int counter = 0;
    boolean invalid = false;
    while(--col >= 0 && --row >= 0) {
      if(isGridEmpty(row, col)) {
        return -2;
      }
      if(this.board[row][col] == team.getNext().getDisk()) { // find teammate's disk
        invalid = true; // invalid
      }
      if(this.board[row][col] == team.getCurrent().getDisk()) { // find your disk;
        return !invalid ? counter : -1;
      }
      counter++;
    }
    return -2;
  }
  
  // Place the initial disks 
  public void initBoard(Team pt, Team ct) {
    int midCol = (int)(SIZE/2);
    int midRow = (int)(SIZE/2);
    //Player[] players = new Player[] { pt.getCurrent(), ct.getCurrent(), pt.getNext(), ct.getNext() };
    char[] colors = new char[] { 'R', 'G', 'Y', 'B' };
    
    for(int i = -2; i < 2; i++) {
      int r = Math.abs(i) % 2 == 0 ? 0: 1; // Row number
      for(int j = -2; j < 2; j++) {
        int p = (r == 0) ? ((Math.abs(j) % 2 == 0) ? 0 : 1): ((Math.abs(j) % 2 == 0) ? 2 : 3); // pick player
        board[midRow + i][midCol + j] = colors[p];
        this.occupied++;
      }
    }
  }
  
  @Override
  protected GameBoard clone() {
    GameBoard clone = null;
    try {
      clone = (GameBoard) super.clone();
      clone.board = new char[SIZE][SIZE];      
      for(int i = 0; i < this.board.length; i++) {
        System.arraycopy(this.board[i], 0, clone.board[i], 0, this.board.length);
      }
      clone.occupied = this.occupied;
    } catch (CloneNotSupportedException e) {
      System.out.println("Cloning Error");
    }
    return clone;
  }
  
  private String addLine() {
    String s = "  +";
    for(int i = 0; i < SIZE; i++) {
      s += "---+";
    }
    s += "\n";
    return s;
    }
    
    public String toString() {
      String boardDisplay = "  ";    
      
      for(int i = 0; i < SIZE; i++) {
        boardDisplay += "  " + i + " ";
      }   
      boardDisplay += "\n" + addLine();
      
      for(int i = 0; i < SIZE; i++) {
        boardDisplay += i + " | ";
        for(int j = 0; j < SIZE; j++) {
          boardDisplay += board[i][j] + " | ";
        }
        boardDisplay += "\n" + addLine();
      }
      
      return boardDisplay;
    }
}