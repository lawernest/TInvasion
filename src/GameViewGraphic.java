import javax.swing.JFrame;
import java.awt.BorderLayout;

public class GameViewGraphic extends JFrame{
  
  private static final long serialVersionUID = 4L;
  private GameViewPanel panel;
  private GameModel model;
  
  public GameViewGraphic(GameModel m, int width, int height) {
    this.panel = new GameViewPanel(m, 50, 50);
    this.model = m;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(width, height);
    setTitle("TInvasion");
    add(panel, BorderLayout.CENTER);
    setResizable(false);
    setVisible(true);
  } 
  
  public void render() {
    this.panel.setMoves(this.model.findValidMoves());
    panel.repaint();
    try {
      Thread.sleep(100);
    } catch (Exception e) {}
  }
}