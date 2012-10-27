package gerentes;

import entidades.Entidade;
import entidades.EntidadeNeutra;
import entidades.Humano;
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
        MapCell[][] map = Mapa.getMap();
        for (int j = 0; j < map[0].length; j++) {
            for (int i = 0; i < map.length; i++) {
                MapCell cell = map[i][j];
                for (Entidade e : cell.getEntidades()) {
                    if (e.getCurrentCell().equals(cell)) {
                        e.draw(g);
                    }
                }
            }
        }
        draw(g, selecao.getBoxSelecao());
        drawEntidadeFocada(g);
        drawEntidadesSelecionadas(g);
        drawMenus(g);
    }

    private void draw(Graphics g, Shape s, Color c) {
        if (s != null) {
            if (c != null) {
                g.setColor(c);
            }
            g.draw(s);
        }
    }

    private void draw(Graphics g, Shape s) {
        draw(g, s, null);
    }

    private void drawPathFound(Graphics g, Entidade e) {
        g.setLineWidth(2);
        draw(g, e.getGerenteMov().getPathfoundLine(), Color.orange);
        g.setLineWidth(1);
    }

    private void drawEntidadeFocada(Graphics g) {
        if (selecao.getEntidadeFocada() != null) {
            draw(g, selecao.getEntidadeFocada().getBox(), Color.green);
        }
    }

    private void drawEntidadesSelecionadas(Graphics g) {
        if (selecao.isHumanosSelecionados()) {
            for (Humano h : selecao.getHumanosSelecionados()) {
                draw(g, h.getBox(), Color.yellow);
            }
        } else if (selecao.isConstrucaoSelecionada()) {
            draw(g, selecao.getConstrucaoSelecionada().getBox(), Color.yellow);
        }
    }

    private void drawMenus(Graphics g) {
        menu.draw(g);
    }
}
