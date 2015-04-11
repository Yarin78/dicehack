package station;

import java.awt.*;

import yarin.algorithms.geometry.Line;
import yarin.algorithms.geometry.LineD;
import yarin.viz.GeoVisualizer;

public class GameVisualizer {

  private final static int VIZ_WIDTH  = 800;
  private final static int VIZ_HEIGHT = 600;
  private final static int VIZ_MARGIN = 20;

  private Game game;

  public GameVisualizer(Game game) {
    this.game = game;
  }

  private int scaleX(int x) {
    return (VIZ_WIDTH - 2 * VIZ_MARGIN) * x / game.getXsize() + VIZ_MARGIN;
  }

  private int scaleY(int y) {
    return (VIZ_HEIGHT - 2 * VIZ_MARGIN) * y / game.getYsize() + VIZ_MARGIN;
  }

  public void show() {
    GeoVisualizer viz = new GeoVisualizer(false);
    viz.setVisibilityArea(0, 0, VIZ_WIDTH, VIZ_HEIGHT);

    for (int y = 0; y <= game.getYsize(); y++) {
      for (int x = 0; x <= game.getXsize(); x++) {
        int xs = scaleX(x), ys = scaleY(y);
        int xs1 = scaleX(x+1), ys1 = scaleY(y + 1);
        if (x+1 <= game.getXsize()) {
          viz.addLine(new Line(xs, ys, xs1, ys), Color.black, game.isSet(x, y, GameImpl.Direction.DOWN) ? 10 : 1);
        }
        if (y+1 <= game.getYsize()) {
          viz.addLine(new Line(xs, ys, xs, ys1), Color.black, game.isSet(x, y, GameImpl.Direction.LEFT) ? 10 : 1);
        }
        if (x < game.getXsize() && y < game.getYsize() && game.getOwner(x, y) >= 0) {
          viz.addRect(new LineD(xs, ys, xs1, ys1), game.getOwner(x, y) == 0 ? Color.red : Color.blue);
        }
      }
    }
    viz.show();
  }
}
