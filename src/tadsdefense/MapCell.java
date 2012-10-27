package tadsdefense;

import entidades.Entidade;
import entidades.Humano;
import gerentes.Mapa;
import java.util.LinkedList;
import org.newdawn.slick.geom.Rectangle;
import util.Const;
import util.Pos;
import util.Util;

public class MapCell {

    class GerentePosicionamento {

        private Humano[] pos;

        protected GerentePosicionamento() {
            pos = new Humano[MapCell.MAX_HUMANOS];
        }

        private int getFreePos() {
            int i = 0;
            for (Humano h : pos) {
                if (h == null) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public void add(Humano h) {
            int i = getFreePos();
            h.setCellPos(i);
            pos[i] = h;
        }

        public void remove(Humano h) {
            pos[h.getCellPos()] = null;
        }
    }
    public static final int MAX_HUMANOS = 1;
    private int lx;
    private int ly;
    private Rectangle box;
    private LinkedList<Entidade> entidades;
    private GerentePosicionamento gerentePos;

    public MapCell(int lx, int ly) {
        this.box = new Rectangle(Mapa.getDx()+(lx * Const.TILE_WIDTH), Mapa.getDy()+ly * Const.TILE_HEIGHT, Const.TILE_WIDTH, Const.TILE_HEIGHT);
        this.lx = lx;
        this.ly = ly;
        this.gerentePos = new GerentePosicionamento();
        this.entidades = new LinkedList();
    }

    public void addEntidade(Entidade entidade) {
        entidades.add(entidade);
        if (entidade instanceof Humano) {
            gerentePos.add((Humano) entidade);
        }
    }

    public void removeEntidade(Entidade entidade) {
        if (entidade instanceof Humano) {
            gerentePos.remove((Humano) entidade);
        }
        entidades.remove(entidade);
    }

    public boolean isCaminhavel(Entidade e) {
        if (entidades.isEmpty()) {
            return true;
        } else {
            int me = (e.getCurrentCell().equals(this)) ? 1 : 0;
            return entidades.getFirst() instanceof Humano && entidades.size() - me < MAX_HUMANOS;
        }

    }

    public Pos getPosXY(Entidade e) {
        switch (e.getCellPos()) {
            case 0:
                return new Pos(getBox().getCenterX(), getBox().getCenterY());
            case 1:
                return new Pos(getBox().getCenterX() + 10, getBox().getCenterY() + 5);
            case 2:
                return new Pos(getBox().getCenterX() - 10, getBox().getCenterY() + 5);
        }
        return null;
    }

    public int getNumHumanos() {
        return getEntidades().size();
    }

    public Rectangle getBox() {
        return box;
    }

    public int getLx() {
        return lx;
    }

    public int getLy() {
        return ly;
    }

    public LinkedList<Entidade> getEntidades() {
        return entidades;
    }
    
    public void updatePos(){
        box.setX(Mapa.getDx()+(lx * Const.TILE_WIDTH));
        box.setY(Mapa.getDy()+(ly * Const.TILE_HEIGHT));
    }

}
