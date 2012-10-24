package gerentes;

import util.Util;
import entidades.Entidade;
import entidades.Humano;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
import tadsdefense.Game;
import tadsdefense.MapCell;

public class GerenteMapa {

    private static ArrayList<Entidade> allEntidades;
    private static MapCell[][] map;
    private static TiledMap tiledMap;

    public static void init() throws SlickException {
        allEntidades = new ArrayList();
        setTiledMap(new TiledMap("res/tileset.tmx", "res"));
        Util.TOTAL_TILE_X = getTiledMap().getWidth();
        Util.TOTAL_TILE_Y = getTiledMap().getHeight();
        map = new MapCell[Util.TOTAL_TILE_X][Util.TOTAL_TILE_Y];
        for (int x = 0; x < Util.TOTAL_TILE_X; x++) {
            for (int y = 0; y < Util.TOTAL_TILE_Y; y++) {
                map[x][y] = new MapCell(x, y);
            }
        }
    }

    public static MapCell getCell(int lx, int ly) {
        return mapLimitsOk(lx, ly) ? getMap()[lx][ly] : null;
    }

    public static MapCell[][] getMap() {
        return map;
    }

    public static int getTileLx(int x) {
        return (int) Math.floor(x / Util.TILE_WIDTH);
    }

    public static int getTileLy(int y) {
        return (int) Math.floor(y / Util.TILE_HEIGHT);
    }

    public static boolean isCaminhoOk(LinkedList<MapCell> movs, Entidade e) {
        boolean isCaminhoOk = true;
        for (MapCell cell : movs) {
            if (!cell.isCaminhavel(e)) {
                isCaminhoOk = false;
            }
        }
        return isCaminhoOk;
    }

    public static TiledMap getTiledMap() {
        return tiledMap;
    }

    public static void setTiledMap(TiledMap aTiledMap) {
        tiledMap = aTiledMap;
    }
    static ArrayList<MapCell> desenha = new ArrayList();

    public static void getCelulaMaisProxima(MapCell[][] cells, Entidade e) {
        int lx = e.getLx();
        int ly = e.getLy();
        int cx0 = cells[0][0].getLx();
        int cx1 = cells[cells.length - 1][0].getLx();
        int cy0 = cells[0][0].getLy();
        int cy1 = cells[0][cells[0].length - 1].getLy();
        int largura = cx1 - cx0 + 1;
        int altura = cy1 - cy0 + 1;
        int indice_x = e.getLx() - cx0;
        int indice_y = e.getLy() - cy0;

        int refV;
        if (ly < cy0) {
            refV = 0;
        } else if (ly > cy1) {
            refV = 2;
        } else {
            refV = 1;
        }
        int refH;
        if (lx < cx0) {
            refH = 0;
        } else if (lx > cx1) {
            refH = 2;
        } else {
            refH = 1;
        }

        //Vizinhos
        MapCell d0 = getCell(cx0 - 1, cy0 - 1);
        MapCell[] cima = getVizinhos(0, cx0, cx1, cy0, cy1);
        MapCell d1 = getCell(cx1 + 1, cy0 - 1);
        MapCell[] direita = getVizinhos(1, cx0, cx1, cy0, cy1);
        MapCell d2 = getCell(cx1 + 1, cy1 + 1);
        MapCell[] baixo = getVizinhos(2, cx0, cx1, cy0, cy1);
        MapCell d3 = getCell(cx0 - 1, cy1 + 1);
        MapCell[] esquerda = getVizinhos(3, cx0, cx1, cy0, cy1);

        ArrayList<MapCell> l = new ArrayList();

        //d0-cima-d1-direita-d2-baixo-d3-esquerda
        //1-largura-1-altura-1-largura-1-altura
        l.add(d0);
        l.addAll(Arrays.asList(cima));
        l.add(d1);
        l.addAll(Arrays.asList(direita));
        l.add(d2);
        l.addAll(Arrays.asList(baixo));
        l.add(d3);
        l.addAll(Arrays.asList(esquerda));

        int i_diag0 = 0;
        int i_cima = i_diag0 + 1 + indice_x;
        int i_diag1 = i_cima + largura;
        int i_direita = i_diag1 + 1 + indice_y;
        int i_diag2 = i_direita + altura;
        int i_baixo = i_diag2 + 1 + indice_x;
        int i_diag3 = i_baixo + largura;
        int i_esquerda = i_diag3 + 1 + indice_y;
        int indice = 0;
        if (refH == 0 && refV == 0) {
            indice = i_diag0;
        } else if (refH == 1 && refV == 0) {
            indice = i_cima;
        } else if (refH == 2 && refV == 0) {
            indice = i_diag1;
        } else if (refH == 2 && refV == 1) {
            indice = i_direita;
        } else if (refH == 2 && refV == 2) {
            indice = i_diag2;
        } else if (refH == 1 && refV == 2) {
            indice = i_baixo;
        } else if (refH == 0 && refV == 2) {
            indice = i_diag3;
        } else {
            indice = i_esquerda;
        }

        LinkedList<MapCell> listaOrdenada = new LinkedList();
        List<MapCell> lista0 = l.subList(indice, l.size());
        List<MapCell> lista1 = l.subList(0, indice);
        listaOrdenada.addAll(lista0);
        listaOrdenada.addAll(lista1);

        LinkedList<MapCell> listaFinal = new LinkedList();
        int size = listaOrdenada.size();
        for (int i = 0; i < size; i++) {
            listaFinal.add(listaOrdenada.remove((i % 2 == 0) ? 0 : listaOrdenada.size() - 1));
        }
        for (MapCell cell : listaFinal) {
            System.out.println(cell.getLx() + "," + cell.getLy());
        }


    }

    private static MapCell[] inverterArray(MapCell[] arr) {
        MapCell[] aux = new MapCell[arr.length];
        for (int i = 0; i < arr.length; i++) {
            aux[i] = arr[arr.length - i - 1];
        }
        return aux;
    }

    private static MapCell[] getVizinhos(int d, int cx0, int cx1, int cy0, int cy1) {
        MapCell[] vizinhos;
        if (d == 0 || d == 2) {
            int largura = cx1 - cx0 + 1;
            vizinhos = new MapCell[largura];
            int y = (d == 0) ? cy0 - 1 : cy1 + 1;
            for (int x = 0; x < largura; x++) {
                vizinhos[x] = getCell(cx0 + x, y);
            }
            if (d == 2) {
                vizinhos = (MapCell[]) inverterArray(vizinhos);
            }

        } else {
            int altura = cy1 - cy0 + 1;
            vizinhos = new MapCell[altura];
            int x = (d == 1) ? cx1 + 1 : cx0 - 1;
            for (int y = 0; y < altura; y++) {
                vizinhos[y] = getCell(x, cy0 + y);
            }
            if (d == 3) {
                vizinhos = (MapCell[]) inverterArray(vizinhos);
            }
        }
        return vizinhos;
    }
    /*
     * Busca a celula vizinha mais proxima de 'cell' e que seja possivel de movimento 
     */

    public static MapCell getVizinhoWalkable(MapCell cell, Entidade e) {
        int lx = cell.getLx();
        int ly = cell.getLy();
        MapCell[] vizinhos = {
            getCell(lx, ly - 1),
            getCell(lx + 1, ly),
            getCell(lx, ly + 1),
            getCell(lx - 1, ly)
        };
        if (e.getLy() < ly && vizinhos[0].isCaminhavel(e) && vizinhos[0] != null) {
            return vizinhos[0];
        } else if (e.getLy() > ly && vizinhos[2].isCaminhavel(e) && vizinhos[2] != null) {
            return vizinhos[2];
        } else if (e.getLx() < lx && vizinhos[3].isCaminhavel(e) && vizinhos[3] != null) {
            return vizinhos[3];
        } else if (vizinhos[1].isCaminhavel(e) && vizinhos[1] != null) {
            return vizinhos[1];
        } else {
            return null;
        }
    }

    public static ArrayList<Humano> getHumanosSelecionados(Rectangle boxSelecao) {
        ArrayList<Humano> humanos = new ArrayList();
        for (Entidade e : getAllEntidades()) {
            if (e instanceof Humano) {
                if (Util.contains(boxSelecao, e.getBox()) || e.getBox().intersects(boxSelecao)) {
                    humanos.add((Humano) e);
                }
            }
        }
        return humanos;
    }

    public static ArrayList<Entidade> getAllEntidades() {
        return allEntidades;
    }

    public static int getDistancia(MapCell cell0, MapCell cell1) {
        return Math.abs(cell0.getLx() - cell1.getLx()) + Math.abs(cell0.getLy() - cell1.getLy());
    }

    public static MapCell[][] getVisao(Entidade e) {
        MapCell[][] visao = e.getVisao();
        int raio = e.getRaioVisao();
        int x0 = e.getLx() - raio;
        int x1 = e.getLx() + raio;
        int y0 = e.getLy() - raio;
        int y1 = e.getLy() + raio;
        for (int i = x0, x = 0; i <= x1; i++, x++) {
            for (int j = y0, y = 0; j <= y1; j++, y++) {
                visao[x][y] = mapLimitsOk(i, j) ? getCell(i, j) : null;
            }
        }
        return visao;
    }

    private static boolean mapLimitsOk(int lx, int ly) {
        return lx >= 0 && lx < Util.TOTAL_TILE_X && ly >= 0 && ly < Util.TOTAL_TILE_Y;
    }

    public static void addEntidade(Entidade e) {
        allEntidades.add(e);
    }
}
