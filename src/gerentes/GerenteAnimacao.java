package gerentes;

import entidades.Entidade;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import util.Pos;
import util.Util;

public class GerenteAnimacao {

    private Entidade entidade;
    private int currentAnim = 0;
    private Animation[] anims;
    private Animation hitAnim;
    
    
    
    
    public Image tiroAnim;
    Vector2f delta;
    Circle circComp;
    Pos tiroPos;

    public GerenteAnimacao(Entidade e, Animation[] anims) {
        this.anims = anims;
        this.entidade = e;
    }

    public GerenteAnimacao(Entidade e) throws SlickException {
        this.entidade = e;
        tiroAnim = new Image("res/tiro.png");
    }

    public void troca(int indice) {
        currentAnim = indice;
    }

    public void stop() {
        anims[currentAnim].stop();
    }

    public void play() {
        anims[currentAnim].start();
    }

    public Animation getAnimation() {
        return anims[currentAnim];
    }

    public void playAtaca(int ladoAlvo) {
        if (!isPlayingAtk()) {
            currentAnim = 4 + ladoAlvo;
        }
    }

    public boolean isPlayingAtk() {
        return currentAnim >= 4;
    }

    public void stopAtaca() {
        if (isPlayingAtk()) {
            currentAnim = 0;
            stop();
        }
    }

    public int getIndice() {
        return currentAnim;
    }

    public void playHitAnim() {
        if (getHitAnim().isStopped()) {
            getHitAnim().restart();
        }
    }

    public Animation getHitAnim() {
        return hitAnim;
    }

    public void setHitAnim(Animation hitAnim) {
        this.hitAnim = hitAnim;
        hitAnim.setLooping(false);
        hitAnim.stop();
    }

    public void playTiroAnim(Entidade e) {
        if (tiroPos == null) {
            int deltaX = e.getCenterX() - entidade.getCenterX();
            int deltaY = e.getCenterY() - entidade.getCenterY();
            Line l = new Line(e.getCenterX(), e.getCenterY(), entidade.getCenterX(), entidade.getCenterY());
            circComp = new Circle(l.getCenterX(), l.getCenterY(), (int) (l.length() / 2));
            delta = new Vector2f(deltaX, deltaY);
            delta.normalise();
            tiroAnim.setRotation((float) delta.getTheta());
            tiroPos = new Pos(entidade.getCenterX(), entidade.getCenterY());
        }
    }

    public void draw() {
        if (tiroPos != null) {
            int x = tiroPos.getX();
            int y = tiroPos.getY();
            x += delta.getX() * 3;
            y += delta.getY() * 3;
            tiroPos.setX(x);
            tiroPos.setY(y);
            if (Util.contains(circComp, x, y)) {
                tiroAnim.draw(x, y);
            } else {
                tiroPos = null;
                circComp = null;
                delta = null;
            }
        }
    }
}
