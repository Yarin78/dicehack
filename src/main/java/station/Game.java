package station;

import java.util.Collection;
import java.util.List;

public interface Game {

  int getOwner(int x, int y);

  boolean isSet(int x, int y, Direction direction);

  Collection<GameImpl.Move> getLegalMoves();

  List<GameImpl.Move> generateLegalMoves();

  int getScore(int player);

  int doMove(GameImpl.Move move);

  int getXsize();

  int getYsize();

  int[][] getGrid();

  int getTurn();

  enum Direction {
    UP(1),
    DOWN(2),
    LEFT(4),
    RIGHT(8);

    final int flag;

    Direction(int flag) {
      this.flag = flag;
    }
  }
}
