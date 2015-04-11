package station;

import station.players.FirstMovePlayer;
import station.players.GreedyPlayer;
import station.players.RandomPlayer;
import station.players.SafePlayer;

public class Simulator {

  public static void main(String[] args) {
    Player random = new RandomPlayer(0);
    Player firstMove = new FirstMovePlayer();
    Player greedy = new GreedyPlayer();
    Player safePlayer = new SafePlayer(); // This player is best by far

//    new Simulator().runSingleGame(4, 4, random, greedy, true);
//    new Simulator().runManyGames(100, 4, 4, random, greedy);

//    new Simulator().runSingleGame(4, 4, greedy, safePlayer, true);
//    new Simulator().runManyGames(100, 4, 4, greedy, safePlayer);
  }

  private Game runSingleGame(int xsize, int ysize, Player p1, Player p2, boolean viz) {
    Player[] players = {p1, p2};

    Game game = new GameImpl(xsize, ysize);

    while (game.getLegalMoves().size() > 0) {
      if (viz) {
        System.out.printf("Player %d to move (%s)%n", game.getTurn() + 1,
                          players[game.getTurn()].getClass().getSimpleName());

        new GameVisualizer(game).show();
      }
      GameImpl.Move move = players[game.getTurn()].getMove(new ReadOnlyGame(game));
      game.doMove(move);
    }

    if (viz) {
      System.out.println("Game finished; " + game.getScore(0) + " - " + game.getScore(1));
      new GameVisualizer(game).show();
    }

    return game;
  }

  private void runManyGames(int games, int xsize, int ysize, Player p1, Player p2) {
    int wins[] = new int[2];
    int draws = 0;

    for (int g = 0; g < games; g++) {
      // Alternate starting order
      Game game = runSingleGame(xsize, ysize, g % 2 == 0 ? p1 : p2, g % 2 == 1 ? p1 : p2, false);

      int diff = game.getScore(0) - game.getScore(1);
      if (diff > 0) {
        wins[g%2]++;
      } else if (diff < 0) {
        wins[1-g%2]++;
      } else {
        draws++;
      }
    }

    System.out.println(String.format("Player 1 wins: %6d  %6.2f%%", wins[0], (wins[0] * 100.0 / games)));
    System.out.println(String.format("Player 2 wins: %6d  %6.2f%%", wins[1], (wins[1] * 100.0 / games)));
    System.out.println(String.format("Draws:         %6d  %6.2f%%", draws, (draws * 100.0 / games)));
  }

}
