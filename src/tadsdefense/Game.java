package tadsdefense;

import entidades.Construcao;
import entidades.Entidade;
import entidades.EntidadeNeutra;
import entidades.Humano;
import entidades.Pedreiro;
import entidades.Propriedades;
import entidades.Time;
import gerentes.GerenteMapa;
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
import util.Util;

public class Game extends BasicGame {

    Humano humano;
    Humano humanoTeste;
    Humano humanoTeste2;
    Construcao castelo;
    Construcao torre;
    Construcao barracks;
    Construcao catapulta;
    Rectangle boxSelecao;
    ArrayList<Humano> humanosSelecionados;

    public Game() throws SlickException {
        super("TADS Defense");
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        GerenteMapa.init();
        GerenteMapa.addEntidade(humano = new Pedreiro(Propriedades.HUMANO, 1, 1, Time.A));
        GerenteMapa.addEntidade(humanoTeste = new Humano(Propriedades.HUMANO, 0, 9, Time.B));
//        GerenteMapa.addEntidade(humanoTeste2 = new Humano(Propriedades.HUMANO, 2, 5, Time.B));
        GerenteMapa.addEntidade(castelo = new Construcao(Propriedades.CASTELO, 5, 5, Time.A));
        GerenteMapa.addEntidade(catapulta = new Construcao(Propriedades.CATAPULTA, 5, 3, Time.A));
        GerenteMapa.addEntidade(torre = new Construcao(Propriedades.TORRE, 2, 2, Time.A));
        GerenteMapa.addEntidade(barracks = new Construcao(Propriedades.BARRACKS, 8, 9, Time.A));
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        Input input = gc.getInput();

        Iterator<Entidade> it = GerenteMapa.getAllEntidades().iterator();
        while (it.hasNext()) {
            Entidade e = it.next();
            e.update();
            if (e.isDead()) {
                if (e instanceof Humano) {
                    humanosSelecionados.remove((Humano) e);
                }
                it.remove();
                e.morri();
            }
        }

        if (input.isMouseButtonDown(0)) {
            if (boxSelecao == null) {
                boxSelecao = new Rectangle(input.getMouseX(), input.getMouseY(), 0, 0);
            } else {
                int x = input.getMouseX();
                int y = input.getMouseY();
                int w = (int) (x - boxSelecao.getX());
                int h = (int) (y - boxSelecao.getY());
                boxSelecao.setWidth(w);
                boxSelecao.setHeight(h);
            }
        } else {
            if (boxSelecao != null) {
                humanosSelecionados = GerenteMapa.getHumanosSelecionados(boxSelecao);
                boxSelecao = null;
            }

        }
        if (input.isMousePressed(1)) {
            int xclique = input.getMouseX();
            int yclique = input.getMouseY();
            MapCell cell = GerenteMapa.getCell(GerenteMapa.getTileLx(xclique), GerenteMapa.getTileLy(yclique));
            Entidade e = null;
            for (Entidade e0 : cell.getEntidades()) {
                if (e0.getBox().contains(xclique, yclique)) {
                    e = e0;
                }
            }
            if (e != null) {
                for (Humano h : humanosSelecionados) {
                    if (!h.equals(e)) {
                        h.ataca(e);
                    }
                }
            } else {
                for (Humano h : humanosSelecionados) {
                    h.desataca();
                    h.goTo(cell);
                }
            }
        }
        
        if (input.isMousePressed(2)){
            int xclique = input.getMouseX();
            int yclique = input.getMouseY();
            MapCell cell = GerenteMapa.getCell(GerenteMapa.getTileLx(xclique), GerenteMapa.getTileLy(yclique));
            new EntidadeNeutra(Propriedades.ARVORE, cell.getLx(), cell.getLy());
        }

    }

    @Override
    public void render(GameContainer gc, Graphics grphcs) throws SlickException {
        GerenteMapa.getTiledMap().render(Util.MAP_X, Util.MAP_Y);
        if (humanosSelecionados != null && !humanosSelecionados.isEmpty()) {
            for (Entidade e : humanosSelecionados) {
                Entidade alvo = e.getGerenteBatalha().getAtacando();
                if (alvo != null) {
                    grphcs.setColor(Color.red);
                    grphcs.draw(alvo.getBox());
                }
//                grphcs.setColor(Color.green);
//                for (MapCell[] m : e.getVisao()) {
//                    for (MapCell c : m) {
//                        if (c != null) {
//                            grphcs.fill(c.getBox());
//                        }
//                    }
//                }
                grphcs.setColor(Color.yellow);
                grphcs.draw(e.getBox());
            }
        }

        int mx = gc.getInput().getMouseX();
        int my = gc.getInput().getMouseY();
        for (MapCell[] m : GerenteMapa.getMap()) {
            for (MapCell cell : m) {
                for (Entidade e : cell.getEntidades()) {
                    if (e.getCurrentCell().equals(cell)) {
                        e.draw();
                        if (!(e instanceof EntidadeNeutra)) {
                            grphcs.setColor(Color.black);
                            grphcs.fill(e.getHpBoxFundo());
                            grphcs.setColor(Color.yellow);
                            grphcs.fill(e.getHpBox());
                            if (e.getBox().contains(mx, my)) {
                                grphcs.setColor(Color.yellow);
                                grphcs.draw(e.getBox());
                            }
//                            if (e.getGerenteMov().getPathfoundLine() != null) {
//                                grphcs.setColor(Color.orange);
//                                grphcs.setLineWidth(2);
//                                grphcs.draw(e.getGerenteMov().getPathfoundLine());
//                            }
//                            grphcs.setLineWidth(1);
                        }
                    }
                }
            }
        }


        if (boxSelecao != null) {
            grphcs.draw(boxSelecao);
        }

    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer gc = new AppGameContainer(new Game());
        gc.setDisplayMode(640, 480, false);
        gc.setMinimumLogicUpdateInterval(50);
        gc.setTargetFrameRate(100);
        gc.start();
    }
}
