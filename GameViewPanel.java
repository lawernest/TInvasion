import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.awt.Point;

public class GameViewPanel extends JPanel {
  
  private static final long serialVersionUID = 4L;
  private GameModel model;
  private int gridW, gridH;
  private List<Point> moves;
  
  public GameViewPanel(GameModel m, int w, int h) {
    this.model = m;
    this.gridW = w;
    this.gridH = h;
    this.moves = null;
  }
  
  public void setMoves(List<Point> list) {
    this.moves = list;
  }
  
  private void placeDisk(Graphics g, int row, int col, Color color) {
    int diskOffset = 5;
    g.setColor(color); 
    g.fillOval(col * gridW + diskOffset, row * gridH + diskOffset, gridW - 10, gridH - 10);
  }
  
  private void displayText(Graphics g) {
    Team team1 = model.getTeam1();
    Team team2 = model.getTeam2();
    g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    
    for(int i = 0; i < GameBoard.SIZE; i++) {
      g.drawString(Integer.toString(i), i * gridW + 70, 40);
    }
    
    String gameMessage = "";
    int val = this.model.gameEnds();
    int width = 250;
    if(val == -1) {
      gameMessage = "Draw";
      width = 275;
    } else if (val == 1) {
      gameMessage = "Team " + this.model.getWinner().getTeamNum() + " Win";
    } else {
      gameMessage = "Team " + this.model.getCurrTeam().getTeamNum() + " Turn";
    }
    int[] teamDisks = this.model.getBoard().countDisks(team1);
    int pScore = teamDisks[0] + teamDisks[1];
    int cScore = this.model.getBoard().getOccupied() - pScore;
    
    if(this.model.getPassCount() > 0 && val != 1) {
      g.drawString("Pass", 275, 600);
    }
    
    g.drawString(gameMessage, width, 575);
    g.drawString("Team 1: " + team1.getCurrent().getDisk() + ", " + team1.getNext().getDisk() , 100, 575);
    g.drawString("Team 2: " + team2.getCurrent().getDisk() + ", " + team2.getNext().getDisk() , 400, 575);
    g.drawString("Score: " + pScore, 100, 600);
    g.drawString("Score: " + cScore, 400, 600);
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    displayText(g);
    
    if(moves != null && this.model.getPassCount() == 0) {
      for(Point move : moves) {
        g.drawOval((int)(move.getY()+1) * gridH + 5, (int)(move.getX()+1) * gridW + 5, gridW - 10, gridH - 10);
      }
    }
    
    for(int i = 0; i < GameBoard.SIZE; i++) {
      g.drawString(Integer.toString(i), 30, i * gridH + 80); 
      for(int j = 0; j < GameBoard.SIZE; j++) {
        g.drawRect((j + 1) * gridH, (i + 1) * gridW, gridW, gridH);
        if(this.model.getBoard().getBoard()[i][j] == 'R') {
          placeDisk(g, i + 1, j + 1, Color.red);
        } else if(this.model.getBoard().getBoard()[i][j] == 'G') {
          placeDisk(g, i + 1, j + 1, Color.green);
        } else if(this.model.getBoard().getBoard()[i][j] == 'Y') {
          placeDisk(g, i + 1, j + 1, Color.yellow);
        } else if(this.model.getBoard().getBoard()[i][j] == 'B') {
          placeDisk(g, i + 1, j + 1, Color.blue);
        }
        g.setColor(Color.black);  
      }
    }
  }
}