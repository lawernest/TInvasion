public class GameView {
  
  private GameModel model;
  
  public GameView(GameModel m) {
    this.model = m;
  } 
    
  public void render() {
    System.out.println("Team 1: " + model.getTeam1().getCurrent().getDisk() + ", " + model.getTeam1().getNext().getDisk());
    System.out.println("Team 2: " + model.getTeam2().getCurrent().getDisk() + ", " + model.getTeam2().getNext().getDisk());
    System.out.println(model.getBoard().getScores()[0] + ", " + model.getBoard().getScores()[1]);
    System.out.println("Current: " + model.getCurrTeam().getCurrent().getDisk());
    System.out.println(displayBoard());
  }
  
  private String addLine() {
    String s = "  +";
    
    for(int i = 0; i < this.model.getBoard().SIZE; i++) {
      s += "---+";
    }
    
    s += "\n";
    return s;
  }
  
  private String displayBoard() {
    String boardDisplay = "  ";    
    char[][] board = this.model.getBoard().getBoard();
    
    for(int i = 0; i < this.model.getBoard().SIZE; i++) {
      boardDisplay += "  " + i + " ";
    }   
    boardDisplay += "\n" + addLine();
    
    for(int i = 0; i < this.model.getBoard().SIZE; i++) {
      boardDisplay += i + " | ";
      for(int j = 0; j < this.model.getBoard().SIZE; j++) {
        boardDisplay += board[i][j] + " | ";
      }
      boardDisplay += "\n" + addLine();
    }
    
    return boardDisplay;
  }
}