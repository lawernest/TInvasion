import java.util.Scanner;
import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.List;
import java.awt.Point;
import java.util.Random;

public class GameController {
  
  private GameModel model;
  private GameViewGraphic view;
  private Scanner input;
  
  public GameController(GameModel m, GameViewGraphic v) {
    this.model = m;
    this.view = v;
    this.input = new Scanner(System.in);
  }
  
  public void waitSpace() {
    System.out.print("Space: ");
    input.nextLine();
  }
  
  public void waitForStart() {
    System.out.print("Press Enter to Start: ");
    input.nextLine();
    this.view.render();
  }
  
  public void getMove() {
    List<Point> validMoves = this.model.findValidMoves();
    System.out.println(validMoves.size());
    if(validMoves.size() == 0) {
      this.model.pass();
      return;
    }
    this.model.resetCounter();
    Point grid = new Point();
    while(true) {
      System.out.print("\nEnter gird to place (col row): ");
      String[] pos = input.nextLine().split(" ");
      try {
        grid.setLocation(Integer.parseInt(pos[1]), Integer.parseInt(pos[0]));
        if(validMoves.contains(grid)) {
          break;
        } else {
          System.out.println("You cannot place here");
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Invalid Input!");
      } catch (NumberFormatException e) {
        System.out.println("Invalid Input!");
      }
    }
    this.model.placeDisk((int)grid.getX(), (int)grid.getY());
  }
  
  public void randomMove() {
    List<Point> validMoves = this.model.findValidMoves();
    if(validMoves.size() == 0) {
      this.model.pass();
      return;
    }
    this.model.resetCounter();
    Random r = new Random();
    int rand = r.nextInt(validMoves.size());
    Point move = validMoves.get(rand);
    this.model.placeDisk((int)move.getX(), (int)move.getY());
  }
}