package station.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import station.Game;
import station.GameImpl;
import station.Player;
import station.ReadOnlyGame;

/**
 * Strategy: Never do a move that gives opponent a chance to score, unless necessary.
 * Picks a random move among these.
 */
public class SafePlayer implements Player {
  Random random = new Random(0);

  public GameImpl.Move getMove(ReadOnlyGame orgGame) {
    // game is now a write:able copy of the original game

    List<GameImpl.Move> allMoves = new ArrayList<GameImpl.Move>(orgGame.getLegalMoves());
    List<GameImpl.Move> safeMoves = new ArrayList<GameImpl.Move>();

    for (GameImpl.Move move : allMoves) {
      Game game = new GameImpl(orgGame);
      if (game.doMove(move) > 0) {
        // We scored so this is a safe move
        safeMoves.add(move);
        continue;
      }

      // Can the opponent now score?
      boolean oppScores = false;
      for (GameImpl.Move oppMove : game.getLegalMoves()) {
        Game oppGame = new GameImpl(game);
        if (oppGame.doMove(oppMove) > 0) {
          oppScores = true;
          break;
        }
      }
      // Nope, so it's a safe move
      if (!oppScores) {
        safeMoves.add(move);
      }
    }

//    System.out.println(safeMoves.size() + " safe moves");
    if (safeMoves.size() > 0) {
      return safeMoves.get(random.nextInt(safeMoves.size()));
    }

    // No safe moves, pick any move
    return allMoves.get(random.nextInt(allMoves.size()));
  }
}
