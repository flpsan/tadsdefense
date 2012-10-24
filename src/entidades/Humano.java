package entidades;

import gerentes.GerenteAnimacao;
import gerentes.GerenteMapa;
import org.newdawn.slick.SlickException;
import util.Util;

public class Humano extends Entidade {

    private GerenteAnimacao gerenteAnimacao;

    public Humano(Propriedades propriedades, int lx, int ly, Time time) throws SlickException {
        super(propriedades, lx, ly, false, time, true);
        setGerenteAnimacao(new GerenteAnimacao(Util.geraHumanoAnims(propriedades.getResDir())));
    }
    private int lastDir;
    private boolean wasMoving;

    public void animHandler() {
        int dir = getGerenteMov().getLastMove();
        boolean isMoving = getGerenteMov().isMoving();
        if (getGerenteBatalha().isAtacando()) {
            getGerenteAnimacao().troca(dir + 4);
        } else {
            if (!isMoving) {
                getGerenteAnimacao().pausa();
            } else if (lastDir != dir) {
                getGerenteAnimacao().troca(dir);
                lastDir = dir;
            } else if (wasMoving != isMoving) {
                getGerenteAnimacao().play();
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
            if (!GerenteMapa.podeAtacar(this, alvo) && getGerenteMov().getMovs().isEmpty()) {
                if (alvo.getPropriedades().isBaseMultipla()){
                    System.out.println("goto1");
                    goTo(GerenteMapa.getCelulaMaisProxima(alvo.getCurrentCells(), this));
                } else {
                    System.out.println("goto2");
                    goTo(GerenteMapa.getCelulaMaisProxima(alvo.getCurrentCell(), this));
                }                
            }
        }
    }

    @Override
    public void draw() {
        getGerenteAnimacao().getAnimation().draw(getX(), getY());
    }

    private GerenteAnimacao getGerenteAnimacao() {
        return gerenteAnimacao;
    }

    private void setGerenteAnimacao(GerenteAnimacao gerenteAnimacao) {
        this.gerenteAnimacao = gerenteAnimacao;
    }

    public void desataca() {
        getGerenteBatalha().desataca();
    }
}
