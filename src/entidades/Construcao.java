package entidades;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import tadsdefense.MapCell;
import util.Util;


public class Construcao extends Entidade {

    Image sprite;
    Image detonado;
    Image original;

    public Construcao(Propriedades propriedades, int lx, int ly, Time time) throws SlickException {
        super(propriedades, lx, ly, true, time, false);
        original = new Image(propriedades.getDirSpriteOriginal());
        detonado = new Image(propriedades.getDirSpriteDetonado());
        sprite = original;
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
    
    public void hit(int hit) {
        super.hit(hit);
        if (super.getHp()<super.getHpMax()/2){
            if (!sprite.equals(detonado)){
                sprite = detonado;
            }
        }
    }

}
