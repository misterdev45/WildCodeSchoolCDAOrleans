package org.wcscda.worms.board.weapons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
// import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;
import org.wcscda.worms.Helper;

public class Hadoken extends AbstractWeapon {
  private static final int hadokenRadius = 50;

  @Override
  public void draw(Graphics2D g, ImageObserver io) {
    Ellipse2D circle =
        new Ellipse2D.Double(
            Helper.getWormX() - hadokenRadius,
            Helper.getWormY() - hadokenRadius,
            2 * hadokenRadius,
            2 * hadokenRadius);

    g.setColor(Color.WHITE);
    g.setStroke(new BasicStroke(1));
    g.draw(circle);
  }
}
