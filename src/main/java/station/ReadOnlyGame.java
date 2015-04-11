package station;

import java.util.Collection;
import java.util.List;

public class ReadOnlyGame implements Game {

  private Game game;

  public ReadOnlyGame(Game game) {
    this.game = game;
  }

  public int getOwner(int x, int y) {
    return game.getOwner(x, y);
  }

  public boolean isSet(int x, int y, Direction direction) {
    return game.isSet(x, y, direction);
  }

  public Collection<GameImpl.Move> getLegalMoves() {
    return game.getLegalMoves();
  }

  public List<GameImpl.Move> generateLegalMoves() {
    return game.generateLegalMoves();
  }

  public int getScore(int player) {
    return game.getScore(player);
  }

  public int doMove(GameImpl.Move move) {
    throw new IllegalStateException();
  }

  public int getXsize() {
    return game.getXsize();
  }

  public int getYsize() {
    return game.getYsize();
  }

  public int[][] getGrid() {
    return game.getGrid();
  }

  public int getTurn() {
    return game.getTurn();
  }
}
