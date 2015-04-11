package station.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import station.Game;
import station.GameImpl;
import station.Player;
import station.ReadOnlyGame;

/**
 * Strategy: If there is a move that gets points, get it! Otherwise random.
 */
public class GreedyPlayer implements Player {

  Random random = new Random(0);

  public GameImpl.Move getMove(ReadOnlyGame orgGame) {
    // game is now a write:able copy of the original game

    List<GameImpl.Move> moves = new ArrayList<GameImpl.Move>(orgGame.getLegalMoves());

    int best = 0;
    GameImpl.Move bestMove = moves.get(random.nextInt(moves.size()));

    for (GameImpl.Move move : moves) {
      Game game = new GameImpl(orgGame);
      int score = game.doMove(move);
      if (score > best) {
        best = score;
        bestMove = move;
      }
    }
    return bestMove;
  }

}
