import java.awt.Point;
import java.util.List;

public class MonteCarlo extends SearchStrategy {
  
  private int numSearch;
  
  public MonteCarlo(int n) {
    this.numSearch = n;
  }
  
  public Point search(State state) {
    MCTree tree = new MCTree(new MCNode(state));
    MCNode node = tree.getRoot();
    int round = 0;
    while(round++ < this.numSearch) {
      // Phase 1: Selection
      MCNode promisingNode = selectPromisingNode(node);
      // Phase 2: Expansion
      if(!promisingNode.getState().gameEnds()) {
        expandNode(promisingNode);
      }
      // Phase 3: Simulation (no new node is created, continue until winner appears)
      MCNode nodeToExplore = promisingNode;
      if(promisingNode.getChildren().size() > 0) {
        nodeToExplore = promisingNode.getRandomChild(); // randomly pick a child node to do a simulation
      }
      Team winner = simulateRandomGame(nodeToExplore); // play the game from the selected node
      // Phase 4: Update
      backPropogation(nodeToExplore, winner);
    }
    
    MCNode nextMove = tree.getRoot().getChildWithHighestUCT();
    /*System.out.println("Monte Carlo");
    System.out.println(nextMove.getState().getAction());
    System.out.println("Visited: " + nextMove.getNumVisited());
    System.out.println("Win: " + nextMove.getNumWin());*/
    return nextMove.getState().getAction();
  }
  
  // Phase 1: Selection
  private MCNode selectPromisingNode(MCNode rootNode) {
    MCNode node = rootNode;
    while(node.getChildren().size() != 0) {
      // pick a node with the highest value
      node = findNodeWithBestUCT(node);
    }
    return node;
  }
  
  // Get a node with highest uct value
  private MCNode findNodeWithBestUCT(MCNode node) {
    int parentVisited = node.getNumVisited();
    List<MCNode> children = node.getChildren();
    MCNode selected = node;
    double maxVal = -1.0;
    for(MCNode child : children) {
      double val = uctValue(child, parentVisited);
      if(val > maxVal) {
        maxVal = val;
        selected = child;
      }
    }
    return selected;
  }
  
  // calculate the uct value for the node
  private double uctValue(MCNode node, int pVisited) {
    double winScore = node.getNumWin();
    int visited = node.getNumVisited();
    if(visited == 0) {
      return Integer.MAX_VALUE;
    }
    double value = winScore/(double)visited + Math.sqrt(2) * Math.sqrt(Math.log(pVisited)/(double)visited);
    return value;
  }
  
  // Phase 2: Expansion
  private void expandNode(MCNode node) {
    List<State> successors = node.getState().expand();
    for(State nextState : successors) {
      MCNode newNode = new MCNode(nextState, node);
      node.addChild(newNode);
    }
  }
  
  // Phase 3: Simulation
  private Team simulateRandomGame(MCNode node) {
    State tempState = new State(node.getState());
    
    while(!tempState.gameEnds()) {
      tempState.playRandomMove();
      int[] teamDisks = tempState.getBoard().countDisks(tempState.getNextTeam());
      if(teamDisks[0] == 0) {
        tempState.getNextTeam().getCurrent().setPlayable(false);
      } 
      if(teamDisks[1] == 0) {
        tempState.getNextTeam().getNext().setPlayable(false);
      }
      tempState.getCurrTeam().changeTurn();
      tempState.switchTeam();
    }
    Team winner = tempState.getWinner();
    return winner;
  }
  
  // Phase 4: Backpropagation
  private void backPropogation(MCNode nodeToExplore, Team winner) {
    MCNode node = nodeToExplore;
    while(node != null) {
      node.increaseNumVisited();
      if(winner == null) {
        node.increaseNumWinForDraw();
      } else if(node.getState().getCurrTeam().equals(winner)) {
        node.increaseNumWin();
      }
      node = node.getParent();
    }
  }
}