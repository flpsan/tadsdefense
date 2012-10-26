package entidades;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import util.Util;

public class Construcao extends Entidade {

    private Image sprite;
    private Image original;
    private SpriteSheet construcao;
    private SpriteSheet detonado;
    private boolean construido;
    private ArrayList<Pedreiro> pedreiros;

    public Construcao(Propriedades propriedades, int lx, int ly, Time time) throws SlickException {
        super(propriedades, lx, ly, true, time, false);
        original = new Image(propriedades.getDirSpriteOriginal());
        detonado = new SpriteSheet(propriedades.getDirSpriteDetonado(), original.getWidth(), original.getHeight());
        construcao = new SpriteSheet(propriedades.getDirSpriteConstrucao(), original.getWidth(), original.getHeight());
        sprite = construcao.getSprite(0, 0);
        construido = false;
        pedreiros = new ArrayList();
    }

    @Override
    public void draw() {
        if (getPropriedades().isImagemMaiorQueBase()) {
            int largura = getPropriedades().getLargura();
            int baseLargura = getPropriedades().getBaseLargura() * Util.TILE_WIDTH;
            int baseAltura = Util.TILE_HEIGHT * getPropriedades().getBaseAltura();
            getSprite().draw(getX() - (largura - baseLargura) / 2, getY() - getPropriedades().getTamanho().getAltura() + baseAltura);
        } else {
            getSprite().draw(getX(), getY());
        }
    }

    public void update() {
        super.update();
        if (!pedreiros.isEmpty()) {
            if (getHp() < getHpMax()) {
                hit(pedreiros.size() * -1);
            }
        }


        if (!construido) {
            if (getHpPercent() >= 1.0) {
                construido = true;
                setSprite(original);
            } else {
                setConstruindoSprite();
            }
        } else {
            if (getHp() < getHpMax()) {
                setDetonadoSprite();
            }
        }
    }

    private void setConstruindoSprite() {
        double n = 1 / (double) getPropriedades().getnConstSprites();
        int i = (int) (getHpPercent() / n);
        setSprite(construcao.getSprite(i, 0));
    }

    private void setDetonadoSprite() {
        double n = 1 / (double) (getPropriedades().getnDetonSprites()+1);
        System.out.println(n+"-"+getHpPercent());
        if (getHpPercent() < 1.0 - n) {
            int i = (int) (getHpPercent() / n);
            setSprite(detonado.getSprite(i, 0));
        }
    }

    private void setSprite(Image s) {
        if (!sprite.equals(s)) {
            sprite = s;
        }
    }

    public ArrayList<Pedreiro> getPedreiros() {
        return pedreiros;
    }

    public Image getSprite() {
        return sprite;
    }

    public boolean isConstruido() {
        return construido;
    }

    public void addPedreiro(Pedreiro pedreiro) {
        pedreiros.add(pedreiro);
    }

    public void removePedreiro(Pedreiro pedreiro) {
        pedreiros.remove(pedreiro);
    }
}
