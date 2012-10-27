package gerentes;

import entidades.Construcao;
import entidades.Entidade;
import entidades.Humano;
import java.util.ArrayList;
import org.newdawn.slick.geom.Rectangle;
import tadsdefense.MapCell;

public class Selecao {

    private ArrayList<Humano> humanosSelecionados;
    private Rectangle boxSelecao;
    private Construcao construcaoSelecionada;

    public void botaoEsquerdo(boolean botaoEsquerdoDown, int x, int y) {
        if (botaoEsquerdoDown) {
            if (getBoxSelecao() == null) {
                boxSelecao = new Rectangle(x, y, 0, 0);
            } else {
                int w = (int) (x - getBoxSelecao().getX());
                int h = (int) (y - getBoxSelecao().getY());
                getBoxSelecao().setWidth(w);
                getBoxSelecao().setHeight(h);
            }
        } else {
            if (getBoxSelecao() != null) {
                humanosSelecionados = Mapa.getHumanosSelecionados(getBoxSelecao());
                boxSelecao = null;
            }
        }
    }

    public void botaoDireito(boolean botaoDireitoDown, int x, int y) {
        if (botaoDireitoDown && isAlgoSelecionado()) {
            MapCell cell = Mapa.getCell(Mapa.getTileLx(x), Mapa.getTileLy(y));
            Entidade e = getEntidadeClicada(cell, x, y);
            for (Humano h : humanosSelecionados) {
                if (e == null) {
                    h.desataca();
                    h.goTo(cell);
                } else {
                    if (!h.equals(e)) {
                        h.ataca(e);
                    }
                }
            }
        }
    }

    private Entidade getEntidadeClicada(MapCell cell, int x, int y) {
        Entidade e = null;
        for (Entidade e0 : cell.getEntidades()) {
            if (e0.getBox().contains(x, y)) {
                e = e0;
            }
        }
        return e;
    }

    public ArrayList<Humano> getHumanosSelecionados() {
        return humanosSelecionados;
    }

    private boolean isAlgoSelecionado() {
        return !humanosSelecionados.isEmpty() || construcaoSelecionada != null;
    }

    public Rectangle getBoxSelecao() {
        return boxSelecao;
    }
}