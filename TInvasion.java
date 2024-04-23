public class TInvasion { 
  
  private GameModel model;
  private GameViewGraphic view;
  private GameController controller;
  
  public TInvasion() {
    this.model = new GameModel();
    this.view = new GameViewGraphic(model, 650, 650);
    this.controller = new GameController(model, view);
  }
  
  public void waitFor() {
    try {
      Thread.sleep(500);
    } catch (Exception e) {}
  }
  
  public void loop() {
    this.controller.waitForStart();
    //double time = 0.0;
    while(this.model.gameEnds() == 0) {
      if(this.model.isPlayerTurn()) {
        this.controller.getMove();
        //this.controller.randomMove();
        //waitFor();
      } else {
        this.model.update();
        //this.controller.waitSpace();
        //waitFor();
      }
      this.model.nextTurn();
      this.view.render();
    }
    this.view.render();
    System.out.println("Game Over");
    
    /*int val = this.model.gameEnds();
    if(val == -1) {
      return val;
    } else if(val == 1) {
      int t = this.model.getWinner().getTeamNum();
      if(t % 2 == 0) {
        return 1;
      }
    }
    return time;*/
  }
  
  public static void main(String args[]) {
    /*int win = 0;
    int tie = 0;
    int gameCount = 0;
    double time = 0.0;
    while(gameCount < 100) {*/
      TInvasion game = new TInvasion();
      game.loop();
    /*  if(result == 1) {
        win++;
      } else if(result == -1) {
        tie++;
      }
      game.view.dispose();
      gameCount++;
      time += result;
    }
    System.out.println("Win: " + win);
    System.out.println("Tie: " + tie);
    System.out.println("Time: " + time/gameCount + "s");
    System.out.println("Total : " + gameCount);*/
  }
}