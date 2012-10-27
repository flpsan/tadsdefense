package entidades;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import gerentes.GMapa;
import tadsdefense.MapCell;

public class EntidadeNeutra extends Entidade {

    Image sprite;

    public EntidadeNeutra(Propriedades propriedades, int lx, int ly) throws SlickException {
        super(propriedades, lx, ly, true, Time.NEUTRO, false);
        sprite = new Image(propriedades.getDirSpriteOriginal());
    }

    @Override
    public void draw() {
        sprite.draw(getX(), getY());
    }
}
