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
            int largura = getPropriedades().getLargura();
            int baseLargura = getPropriedades().getBaseLargura()*Util.TILE_WIDTH;
            int baseAltura = Util.TILE_HEIGHT*getPropriedades().getBaseAltura();
            sprite.draw(getX()-(largura-baseLargura)/2, getY()-getPropriedades().getTamanho().getAltura()+baseAltura);
        } else {
            sprite.draw(getX(), getY());
        }
    }

}
