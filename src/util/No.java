package util;

import tadsdefense.MapCell;

public class No {

    public No pai;
    private MapCell cell;
    public int lx, ly;
    public int g, h;

    public No(MapCell cell) {
        this.cell = cell;
        this.lx = cell.getLx();
        this.ly = cell.getLy();
    }

    public int getF() {
        return g + h;
    }

    public void setPai(No pai) {
        this.pai = pai;
        this.g = pai.g + 1;
    }

    public int compareTo(Object o) {
        No noComp = (No) o;
        return (int) (getF() - noComp.getF());
    }

    public void setaHeuristica(No noFinal) {
        int lx1 = noFinal.lx, ly1 = noFinal.ly;
        h = Math.abs(lx - lx1) + Math.abs(ly - ly1);
    }

    public MapCell getCell() {
        return cell;
    }
}
