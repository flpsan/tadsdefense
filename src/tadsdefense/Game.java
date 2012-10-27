package tadsdefense;

import entidades.Construcao;
import entidades.Entidade;
import entidades.EntidadeNeutra;
import entidades.Humano;
import entidades.Pedreiro;
import entidades.Propriedades;
import entidades.Time;
import gerentes.Camera;
import gerentes.GerenteAnimacao;
import gerentes.Mapa;
import gerentes.GerenteInput;
import gerentes.GerenteRenderizacao;
import gerentes.Menu;
import gerentes.Selecao;
import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import util.Const;
import util.Util;

public class Game extends BasicGame {

    Humano humano;
    Humano humanoTeste;
    Humano humanoTeste2;
    Construcao castelo;
    Construcao torre;
    Construcao barracks;
    Construcao catapulta;
    
    GerenteRenderizacao gerenteRenderizacao;
    GerenteInput gerenteInput;
    Camera camera;
    Selecao selecao;
    Menu menu;
    
    public Game() throws SlickException {
        super("TADS Defense");
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        camera = new Camera(gc);
        menu = new Menu(gc);
        selecao = new Selecao();
        gerenteRenderizacao = new GerenteRenderizacao(camera, selecao, menu);
        gerenteInput = new GerenteInput(camera, selecao, menu);

        Mapa.init();
        Mapa.addEntidade(humano = new Pedreiro(Propriedades.HUMANO, 1, 1, Time.A));
        Mapa.addEntidade(humanoTeste = new Humano(Propriedades.HUMANO, 0, 9, Time.B));
        Mapa.addEntidade(castelo = new Construcao(Propriedades.CASTELO, 5, 5, Time.A));
        Mapa.addEntidade(catapulta = new Construcao(Propriedades.CATAPULTA, 5, 3, Time.A));
        Mapa.addEntidade(torre = new Construcao(Propriedades.TORRE, 2, 2, Time.A));
        Mapa.addEntidade(barracks = new Construcao(Propriedades.BARRACKS, 8, 9, Time.A));

    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        gerenteInput.update(gc);

        Iterator<Entidade> it = Mapa.getAllEntidades().iterator();
        while (it.hasNext()) {
            Entidade e = it.next();
            e.update();
            if (e.isDead()) {
                if (e instanceof Humano) {
                    selecao.getHumanosSelecionados().remove((Humano) e);
                }
                it.remove();
                e.morri();
            }
        }

    }

    @Override
    public void render(GameContainer gc, Graphics grphcs) throws SlickException {
        gerenteRenderizacao.render(gc, grphcs);
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer gc = new AppGameContainer(new Game());
        gc.setDisplayMode(640, 480, false);
        gc.setMinimumLogicUpdateInterval(50);
        gc.setTargetFrameRate(100);
        gc.setShowFPS(false);
        gc.start();
    }
}
