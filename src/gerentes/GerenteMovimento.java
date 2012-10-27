package gerentes;

import entidades.Entidade;
import java.util.ArrayList;
import java.util.LinkedList;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Rectangle;
import tadsdefense.MapCell;
import util.AStar;
import util.No;
import util.Pos;

public class GerenteMovimento {

    private LinkedList<MapCell> movs;
    private Path pathfoundLine;
    private Entidade entidade;
    private boolean moving;
    private int x1;
    private int y1;
    private int lastMove;
    private MapCell nextCell;
    private AStar aStar;
    private int lastPathfAlvoLx;
    private int lastPathfAlvoLy;

    public GerenteMovimento(Entidade entidade) {
        this.entidade = entidade;
        this.movs = new LinkedList();
        this.aStar = new AStar(entidade);
        stop();
    }

    public void stop() {
        setMoving(false);
        setNextCell(null);
        setX1(0);
        setY1(0);
    }

    public boolean isMoving() {
        return moving;
    }

    public int getVelRecomendada(int dir) {
        int velRec;
        int vel = entidade.getPropriedades().getVelocidade();
        if (dir == 0 || dir == 2) {
            velRec = getY1() - getEntidade().getCenterY();
        } else {
            velRec = getX1() - getEntidade().getCenterX();
        }
        velRec = (Math.abs(velRec) > vel) ? vel : Math.abs(velRec);
        return velRec;
    }

    public int[] getDirecaoToMove() {
        int x = getEntidade().getCenterX();
        int y = getEntidade().getCenterY();
        int dir0 = -1, dir1 = -1;
        if (x != getX1()) {
            dir0 = (x < getX1()) ? 1 : 3;
        }
        if (y != getY1()) {
            dir1 = (y < getY1()) ? 2 : 0;
        }
        if (getLastMove() == 0 || getLastMove() == 2) {
            int temp = dir0;
            dir0 = dir1;
            dir1 = temp;
        }
        return new int[]{dir0, dir1};
    }

    public void startMovement(MapCell cell) {
        if (!isMoving()) {
            setMoving(true);
            setX1((int) cell.getBox().getCenterX());
            setY1((int) cell.getBox().getCenterY());
        }
    }

    public void move(int d[], int n) {
        int dir = d[n];
        if (dir == -1 && n == 0) {
            move(d, 1);
        } else {
            int novox = entidade.getCenterX();
            int novoy = entidade.getCenterY();
            int oldx = entidade.getCenterX();
            int oldy = entidade.getCenterY();
            int delta = entidade.getGerenteMov().getVelRecomendada(dir);

            switch (dir) {
                case 0:
                    novoy -= delta;
                    break;
                case 1:
                    novox += delta;
                    break;
                case 2:
                    novoy += delta;
                    break;
                case 3:
                    novox -= delta;
                    break;
            }
            entidade.setCenterX(novox);
            entidade.setCenterY(novoy);
            boolean tocouCelula = getNextCell().getBox().intersects(entidade.getBox());

            if (tocouCelula) {
                if (getNextCell().isCaminhavel(entidade)) {
                    if (entidade.getLx() != getNextCell().getLx() || entidade.getLy() != getNextCell().getLy()) {
                        entidade.setCurrentCell(getNextCell());
                    }
                    setLastMove(dir);
                } else {
                    entidade.setCenterX(oldx);
                    entidade.setCenterY(oldy);
                    if (n == 0 && d[1] == -1) {
                        move(d, 1);
                    } else {
                        stop();
                    }
                }
            } else {
                if (dir != -1) {
                    setLastMove(dir);
                }
            }

        }

    }

    private void rePathfind(MapCell m) {
        startPathfind(m);
    }

    private void rePathfind() {
        startPathfind(lastPathfAlvoLx, lastPathfAlvoLy);
    }

    public void update() {
        if (entidade.isMovel()) {
            if (!isMoving() && !getMovs().isEmpty()) {
                MapCell cell = getMovs().pop();
                setNextCell(cell);
                boolean isCaminhoOk = GMapa.isCaminhoOk(getMovs(), entidade);
                boolean isAlvoOk = GMapa.getCell(lastPathfAlvoLx, lastPathfAlvoLy).isCaminhavel(entidade);
                if (!isAlvoOk) {
                    rePathfind(GMapa.getCelulaMaisProxima(entidade,GMapa.getLimitesDaCelula(GMapa.getCell(lastPathfAlvoLx, lastPathfAlvoLy))));
                } else if (!isCaminhoOk) {
                    rePathfind();
                } else {
                    startMovement(getNextCell());
                }
            }

            if (isMoving() && !chegou()) {
                move(getDirecaoToMove(), 0);
            }
        }

    }

    private boolean chegou() {
        boolean chegou;
        if (getY1() - getEntidade().getCenterY() == 0 && getX1() - getEntidade().getCenterX() == 0) {
            if (getMovs().isEmpty()) {
                Pos posIdeal = entidade.getCurrentCell().getPosXY(entidade);
                if (posIdeal.getX() == entidade.getCenterX() && posIdeal.getY() == entidade.getCenterY()) {
                    chegou = true;
                    stop();
                } else {
                    chegou = false;
                    setX1(posIdeal.getX());
                    setY1(posIdeal.getY());
                }
            } else {
                chegou = true;
                stop();
            }
        } else {
            chegou = false;
        }
        return chegou;
    }

    public void startPathfind(MapCell cell) {
        startPathfind(cell.getLx(), cell.getLy());
    }

    public void startPathfind(int tileLx, int tileLy) {
        lastPathfAlvoLx = tileLx;
        lastPathfAlvoLy = tileLy;
        movs.clear();
        ArrayList<No> nos = aStar.geraMelhorCaminho(entidade.getLx(), entidade.getLy(), tileLx, tileLy);
        pathfoundLine = new Path(entidade.getCurrentCell().getBox().getCenterX(), entidade.getCurrentCell().getBox().getCenterY());
        for (int i = nos.size() - 1; i >= 0; i--) {
            No n = nos.get(i);
            getMovs().add(n.getCell());
            Rectangle tile = n.getCell().getBox();
            getPathfoundLine().lineTo(tile.getCenterX(), tile.getCenterY());
        }

    }

    public LinkedList<MapCell> getMovs() {
        return movs;
    }

    public void setMovs(LinkedList<MapCell> movs) {
        this.setMovs(movs);
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getLastMove() {
        return lastMove;
    }

    public void setLastMove(int lastMove) {
        this.lastMove = lastMove;
    }

    public void setPathfoundLine(Path pathfoundLine) {
        this.pathfoundLine = pathfoundLine;
    }

    public MapCell getNextCell() {
        return nextCell;
    }

    public void setNextCell(MapCell nextCell) {
        this.nextCell = nextCell;
    }

    public Path getPathfoundLine() {
        return pathfoundLine;
    }
}
