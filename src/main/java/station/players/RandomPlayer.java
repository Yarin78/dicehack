package station.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import station.GameImpl;
import station.Player;
import station.ReadOnlyGame;

/**
 * Strategy: Random move!
 */
public class RandomPlayer implements Player {

  private Random random;

  public RandomPlayer() {
    this.random = new Random();
  }

  public RandomPlayer(int seed) {
    this.random = new Random(seed);
  }

  public GameImpl.Move getMove(ReadOnlyGame game) {
    List<GameImpl.Move> moves = new ArrayList<GameImpl.Move>(game.getLegalMoves());

    return moves.get(random.nextInt(moves.size()));
  }
}
