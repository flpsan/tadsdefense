/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Point;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author Felipe
 */
public class Util {

    public static final int MAP_X = 0, MAP_Y = 0, TILE_HEIGHT = 32, TILE_WIDTH = 32;
    public static final int HUMANO_WIDTH = 12, HUMANO_HEIGHT = 26;
    public static int TOTAL_TILE_X, TOTAL_TILE_Y;

    public static Animation[] geraHumanoAnims(String res) throws SlickException {
        SpriteSheet spritesheet = new SpriteSheet(res, HUMANO_WIDTH, HUMANO_HEIGHT);
        Animation norte = new Animation(spritesheet, 0, 0, 1, 0, true, 150, true);
        Animation direita = new Animation(spritesheet, 0, 1, 3, 1, true, 100, true);
        Animation sul = new Animation(spritesheet, 0, 2, 1, 2, true, 150, true);
        Animation esquerda = new Animation(spritesheet, 0, 3, 3, 3, true, 100, true);
        Animation nortef = new Animation(spritesheet, 0, 4, 1, 4, true, 150, true);
        Animation direitaf = new Animation(spritesheet, 0, 5, 1, 5, true, 100, true);
        Animation sulf = new Animation(spritesheet, 0, 6, 1, 6, true, 150, true);
        Animation esquerdaf = new Animation(spritesheet, 0, 7, 1, 7, true, 100, true);
        return new Animation[]{norte, direita, sul, esquerda, nortef, direitaf, sulf, esquerdaf};
    }

    public static boolean contains(Rectangle rA, Rectangle rB) {
        return (rA.getMinX() <= rB.getMinX() && rA.getMaxX() >= rB.getMaxX() && rA.getMinY() <= rB.getMinY() && rA.getMaxY() >= rB.getMaxY());
    }

    public static boolean contains(Circle c, double x, double y) {
        double dist = Point.distance(x, y, c.getCenterX(), c.getCenterY());
        return dist <= c.getRadius();
    }
}
