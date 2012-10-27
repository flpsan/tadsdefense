package gerentes;

import entidades.Entidade;
import entidades.EntidadeNeutra;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import tadsdefense.MapCell;

public class GerenteRenderizacao {

    Camera camera;
    Selecao selecao;
    Menu menu;

    public GerenteRenderizacao(Camera camera, Selecao selecao, Menu menu) {
        this.camera = camera;
        this.selecao = selecao;
        this.menu = menu;
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        Mapa.getTiledMap().render(Mapa.getDx(), Mapa.getDy());
        if (selecao.getHumanosSelecionados() != null && !selecao.getHumanosSelecionados().isEmpty()) {
            for (Entidade e : selecao.getHumanosSelecionados()) {
                Entidade alvo = e.getGerenteBatalha().getAtacando();
                if (alvo != null) {
                    g.setColor(Color.red);
                    g.draw(alvo.getBox());
                }
//                grphcs.setColor(Color.green);
//                for (MapCell[] m : e.getVisao()) {
//                    for (MapCell c : m) {
//                        if (c != null) {
//                            grphcs.fill(c.getBox());
//                        }
//                    }
//                }
                g.setColor(Color.yellow);
                g.draw(e.getBox());
            }
        }

        int mx = gc.getInput().getMouseX();
        int my = gc.getInput().getMouseY();
        for (MapCell[] m : Mapa.getMap()) {
            for (MapCell cell : m) {
                for (Entidade e : cell.getEntidades()) {
                    if (e.getCurrentCell().equals(cell)) {
                        e.draw();
                        if (!(e instanceof EntidadeNeutra)) {
                            g.setColor(Color.black);
                            g.fill(e.getHpBoxFundo());
                            g.setColor(Color.yellow);
                            g.fill(e.getHpBox());
                            if (e.getBox().contains(mx, my)) {
                                g.setColor(Color.yellow);
                                g.draw(e.getBox());
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

        draw(g, selecao.getBoxSelecao());
    }

    private void draw(Graphics g, Shape s, Color c) {
        if (s != null) {
            if (c != null) {
                g.setColor(Color.yellow);
            }
            g.draw(s);
        }
    }
    
    private void draw(Graphics g, Shape s) {
        draw(g,s,null);
    }
}
