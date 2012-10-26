package entidades;

import gerentes.GerenteAnimacao;
import gerentes.GerenteMapa;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import util.Util;

public class Humano extends Entidade {

    private GerenteAnimacao gerenteAnimacao;

    public Humano(Propriedades propriedades, int lx, int ly, Time time) throws SlickException {
        super(propriedades, lx, ly, false, time, true);
        gerenteAnimacao = new GerenteAnimacao(Util.geraHumanoAnims(propriedades.getDirSpriteOriginal()));
        gerenteAnimacao.setHitAnim(new Animation(new SpriteSheet("res/hitHumano.png", 15, 15), 150));
    }
    
    private int lastDir;
    private boolean wasMoving;

    public void animHandler() {
        int dir = getGerenteMov().getLastMove();
        boolean isMoving = getGerenteMov().isMoving();
        if (!gerenteAnimacao.isAtacando()) {
            if (!isMoving) {
                gerenteAnimacao.stop();
            } else if (lastDir != dir) {
                gerenteAnimacao.troca(dir);
                lastDir = dir;
            } else if (wasMoving != isMoving) {
                gerenteAnimacao.play();
            }
        }
        wasMoving = isMoving;
    }

    @Override
    public void update() {
        super.update();
        animHandler();
        getGerenteBatalha().update();
        if (getGerenteBatalha().isAtacando()) {
            Entidade alvo = getGerenteBatalha().getAtacando();
            boolean podeAtacar = GerenteMapa.podeAtacar(this, alvo);
            if (getGerenteMov().getMovs().isEmpty()) {
                if (!podeAtacar) {
                    goTo(GerenteMapa.getCelulaMaisProxima(this, alvo));
                } else if (podeAtacar) {
                    gerenteAnimacao.playAtaca(getGerenteBatalha().getLadoAlvo());
                }
            }
        }
    }

    @Override
    public void draw() {
        gerenteAnimacao.getAnimation().draw(getX(), getY());
        if (gerenteAnimacao.getHitAnim()!=null && !gerenteAnimacao.getHitAnim().isStopped()) {
            gerenteAnimacao.getHitAnim().draw(getX(), getY());
        }
    }


    @Override
    public void ataca(Entidade e) {
        getGerenteBatalha().ataca(e);
    }

    @Override
    public void desataca() {
        gerenteAnimacao.stopAtaca();
        getGerenteBatalha().desataca();
    }

    @Override
    public void hit(int hit) {
        super.hit(hit);
        gerenteAnimacao.playHitAnim();
    }
}
