package station;

public interface Player {

  GameImpl.Move getMove(ReadOnlyGame game);
}
