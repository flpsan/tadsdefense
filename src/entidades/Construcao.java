package entidades;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import tadsdefense.MapCell;
import util.Util;


public class Construcao extends Entidade {

    Image sprite;

    public Construcao(Propriedades propriedades, int lx, int ly, Time time) throws SlickException {
        super(propriedades, lx, ly, true, time, false);
        sprite = new Image(propriedades.getResDir());
    }

    @Override
    public void draw() {
        if (getPropriedades().isImagemMaiorQueBase()) {
            sprite.draw(getX(), getY()-getPropriedades().getTamanho().getAltura()+Util.TILE_HEIGHT);
        } else {
            sprite.draw(getX(), getY());
        }
    }

}
