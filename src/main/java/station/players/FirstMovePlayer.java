package station.players;

import station.GameImpl;
import station.Player;
import station.ReadOnlyGame;

/**
 * Strategy: First available move!
 */
public class FirstMovePlayer implements Player {

  public GameImpl.Move getMove(ReadOnlyGame game) {
    return game.getLegalMoves().iterator().next();
  }
}
