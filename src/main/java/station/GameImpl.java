package station;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class GameImpl implements Game {

  public GameImpl(Game game) {
    this.xsize = game.getXsize();
    this.ysize = game.getYsize();
    this.grid = new int[ysize][xsize];
    this.owner = new int[ysize][xsize];
    for (int y = 0; y < ysize; y++) {
      for (int x = 0; x < xsize; x++) {
        this.grid[y][x] = game.getGrid()[y][x];
        this.owner[y][x] = game.getOwner(x, y);
      }
    }
    this.availableMoves = new ArrayList<Move>(game.getLegalMoves());
  }

  public GameImpl(int xsize, int ysize) {
    this.xsize = xsize;
    this.ysize = ysize;
    grid = new int[ysize][xsize];
    owner = new int[ysize][xsize];
    for (int y = 0; y < ysize; y++) {
      for (int x = 0; x < xsize; x++) {
        owner[y][x] = -1;
      }
    }
    availableMoves = new TreeSet<Move>(generateLegalMoves());
    turn = 0;
  }

  @Getter
  private int xsize, ysize;

  private Collection<Move> availableMoves;

  @Getter
  private int[][] grid;  // Which walls are set
  private int[][] owner; // Which player owns the square? (-1 = no one)

  @Getter
  private int turn;

  public int getOwner(int x, int y) {
    return this.owner[y][x];
  }

  public boolean isSet(int x, int y, Direction direction) {
    if (x >= xsize && direction == Direction.LEFT) {
      x--;
      direction = Direction.RIGHT;
    }
    if (y >= ysize && direction == Direction.DOWN) {
      y--;
      direction = Direction.UP;
    }
    return (this.grid[y][x] & direction.flag) == direction.flag;
  }

  public Collection<Move> getLegalMoves() {
    return Collections.unmodifiableCollection(availableMoves);
  }

  public List<Move> generateLegalMoves() {
    ArrayList<Move> moves = new ArrayList<Move>();
    for (int y = 0; y <= ysize; y++) {
      for (int x = 0; x <= xsize; x++) {
        Move move = new Move(x, y, Direction.LEFT);
        if (move.isLegal()) moves.add(move);

        move = new Move(x, y, Direction.DOWN);
        if (move.isLegal()) moves.add(move);
      }
    }
    return moves;
  }

  public int getScore(int player) {
    int score = 0;
    for (int y = 0; y < ysize; y++) {
      for (int x = 0; x < xsize; x++) {
        if (owner[y][x] == player)
          score++;
      }
    }
    return score;
  }

  public int doMove(Move move) {
    if (!move.isLegal()) {
      throw new RuntimeException("Illegal move");
    }

    int pts = 0;
    switch (move.direction) {
      case DOWN:
        if (move.y - 1 >= 0 && (grid[move.y - 1][move.x] |= Direction.UP.flag) == 15) {
          pts++;
          owner[move.y - 1][move.x] = turn;
        }
        break;
      case UP:
        if (move.y + 1 < ysize && (grid[move.y + 1][move.x] |= Direction.DOWN.flag) == 15) {
          pts++;
          owner[move.y + 1][move.x] = turn;
        }
        break;
      case LEFT:
        if (move.x - 1 >= 0 && (grid[move.y][move.x - 1] |= Direction.RIGHT.flag) == 15) {
          pts++;
          owner[move.y][move.x - 1] = turn;
        }
        break;
      case RIGHT:
        if (move.x + 1 < xsize && (grid[move.y][move.x + 1] |= Direction.LEFT.flag) == 15) {
          pts++;
          owner[move.y][move.x + 1] = turn;
        }
        break;
      default:
        throw new RuntimeException("Illegal move");
    }
    if ((grid[move.y][move.x] |= move.direction.flag) == 15) {
      pts++;
      owner[move.y][move.x] = turn;
    }
    availableMoves.remove(move);
    if (pts == 0) {
      turn = 1 - turn;
    }
    return pts;
  }

  @ToString
  @EqualsAndHashCode
  public class Move implements Comparable<Move> {

    @Getter
    private int x, y;

    @Getter
    private Direction direction;

    public Move(int x, int y, Direction direction) {
      if (direction == Direction.DOWN && y == ysize) {
        y--;
        direction = Direction.UP;
      }
      if (direction == Direction.LEFT && x == xsize) {
        x--;
        direction = Direction.RIGHT;
      }
      // Normalize the move to use DOWN and LEFT if possible
      if (direction == Direction.UP && y < ysize - 1) {
        y++;
        direction = Direction.DOWN;
      }
      if (direction == Direction.RIGHT && x < xsize - 1) {
        x++;
        direction = Direction.LEFT;
      }

      this.direction = direction;
      this.x = x;
      this.y = y;
    }

    public boolean isLegal() {
      if (y < 0 || x < 0 || y >= ysize || x >= xsize) {
        return false;
      }
      if ((grid[y][x] & direction.flag) == direction.flag) {
        return false;
      }
      return true;
    }

    public int compareTo(Move that) {
      if (y != that.y) return y - that.y;
      if (x != that.x) return x - that.x;
      return direction.flag - that.direction.flag;
    }

  }
}
