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
                    goTo(GerenteMapa.getCelulaMaisProxima(this,alvo));
                } else if (podeAtacar) {
                    System.out.println("teste");
                    gerenteAnimacao.playAtaca(getGerenteBatalha().getLadoAlvo());
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
    public void hit(int hit){
        super.hit(hit);
        System.out.println("teste");
    }
}
