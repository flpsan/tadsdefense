package entidades;

import gerentes.GerenteConstrucao;
import org.newdawn.slick.SlickException;

public class Pedreiro extends Humano {

    GerenteConstrucao gerenteConstrucao;

    public Pedreiro(Propriedades propriedades, int lx, int ly, Time time) throws SlickException {
        super(propriedades, lx, ly, time);
        gerenteConstrucao = new GerenteConstrucao(this);
    }

    
    public void update(){
        super.update();
        gerenteConstrucao.update();
    }
    
    public void ataca(Entidade e) {
        gerenteConstrucao.alvoConstrucao = (Construcao)e;
    }

    public void desataca() {
        gerenteConstrucao.paraConstrucao();
    }
}
