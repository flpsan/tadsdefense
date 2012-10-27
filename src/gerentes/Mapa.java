package gerentes;

import entidades.Entidade;
import entidades.Humano;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
import tadsdefense.MapCell;
import util.Const;
import util.Pos;
import util.Util;

public class Mapa {

    private static ArrayList<Entidade> entidades;
    private static MapCell[][] map;
    private static TiledMap tiledMap;
    private static Pos deslocamentoMapa;

    public static void init() throws SlickException {
        deslocamentoMapa = new Pos(0, Const.MENUSUP_ALTURA);
        entidades = new ArrayList();
        setTiledMap(new TiledMap("res/tileset.tmx", "res"));
        Const.TOTAL_TILE_X = getTiledMap().getWidth();
        Const.TOTAL_TILE_Y = getTiledMap().getHeight();
        map = new MapCell[Const.TOTAL_TILE_X][Const.TOTAL_TILE_Y];
        for (int lx = 0; lx < Const.TOTAL_TILE_X; lx++) {
            for (int ly = 0; ly < Const.TOTAL_TILE_Y; ly++) {
                map[lx][ly] = new MapCell(lx, ly);
            }
        }
    }

    public static int getDx() {
        return deslocamentoMapa.getX();
    }

    public static int getDy() {
        return deslocamentoMapa.getY();
    }

    public static MapCell getCell(int lx, int ly) {
        return mapLimitsOk(lx, ly) ? getMap()[lx][ly] : null;
    }

    public static MapCell[][] getMap() {
        return map;
    }

    public static int getTileLx(int x) {
        return (int) Math.floor((x - Mapa.getDx()) / Const.TILE_WIDTH);
    }

    public static int getTileLy(int y) {
        return (int) Math.floor((y - Mapa.getDy()) / Const.TILE_HEIGHT);
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

    public static MapCell getCelulaMaisProxima(Entidade queAtaca, Entidade alvo) {
        return getCelulaMaisProxima(queAtaca, getLimitesDaEntidade(alvo));
    }

    public static int[] getLimitesDaEntidade(Entidade e) {
        int cx0, cx1, cy0, cy1;
        if (e.getPropriedades().isBaseMultipla()) {
            MapCell[][] cells = e.getCurrentCells();
            cx0 = cells[0][0].getLx();
            cx1 = cells[cells.length - 1][0].getLx();
            cy0 = cells[0][0].getLy();
            cy1 = cells[0][cells[0].length - 1].getLy();
        } else {
            MapCell cell = e.getCurrentCell();
            cx0 = cell.getLx();
            cx1 = cell.getLx();
            cy0 = cell.getLy();
            cy1 = cell.getLy();
        }
        return new int[]{cx0, cx1, cy0, cy1};
    }

    public static int[] getLimitesDaCelula(MapCell cell) {
        return new int[]{cell.getLx(), cell.getLx(), cell.getLy(), cell.getLy()};
    }

    public static Pos getPosicaoReferencia(Entidade e, int[] limites) {
        int refV, refH;
        int cx0 = limites[0];
        int cx1 = limites[1];
        int cy0 = limites[2];
        int cy1 = limites[3];
        int lx = e.getLx();
        int ly = e.getLy();
        if (ly < cy0) {
            refV = 0;
        } else if (ly > cy1) {
            refV = 2;
        } else {
            refV = 1;
        }
        if (lx < cx0) {
            refH = 0;
        } else if (lx > cx1) {
            refH = 2;
        } else {
            refH = 1;
        }
        return new Pos(refH, refV);
    }

    public static MapCell getCelulaMaisProxima(Entidade e, int[] limites) {
        int cx0 = limites[0];
        int cx1 = limites[1];
        int cy0 = limites[2];
        int cy1 = limites[3];
        int largura = cx1 - cx0 + 1;
        int altura = cy1 - cy0 + 1;
        int indice_xcima = e.getLx() - cx0;
        int indice_ydireita = e.getLy() - cy0;
        int indice_xbaixo = cx1 - e.getLx();
        int indice_yesquerda = cy1 - e.getLy();



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

        l.add(d0);
        l.addAll(Arrays.asList(cima));
        l.add(d1);
        l.addAll(Arrays.asList(direita));
        l.add(d2);
        l.addAll(Arrays.asList(baixo));
        l.add(d3);
        l.addAll(Arrays.asList(esquerda));

        int i_diag0 = 0;
        int i_cima = i_diag0 + 1;
        int i_diag1 = i_cima + largura;
        int i_direita = i_diag1 + 1;
        int i_diag2 = i_direita + altura;
        int i_baixo = i_diag2 + 1;
        int i_diag3 = i_baixo + largura;
        int i_esquerda = i_diag3 + 1;
        int indice = 0;

        Pos posRef = getPosicaoReferencia(e, limites);
        int refH = posRef.getX();
        int refV = posRef.getY();

        if (refH == 0 && refV == 0) {
            indice = i_diag0;
        } else if (refH == 1 && refV == 0) {
            indice = i_cima + indice_xcima;
        } else if (refH == 2 && refV == 0) {
            indice = i_diag1;
        } else if (refH == 2 && refV == 1) {
            indice = i_direita + indice_ydireita;
        } else if (refH == 2 && refV == 2) {
            indice = i_diag2;
        } else if (refH == 1 && refV == 2) {
            indice = i_baixo + indice_xbaixo;
        } else if (refH == 0 && refV == 2) {
            indice = i_diag3;
        } else {
            indice = i_esquerda + indice_yesquerda;
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
            if (cell != null) {
                if (cell.isCaminhavel(e)) {
                    return cell;
                }
            }
        }
        return null;


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
        return entidades;
    }

    public static int getDistancia(MapCell cell0, MapCell cell1) {
        return Math.abs(cell0.getLx() - cell1.getLx()) + Math.abs(cell0.getLy() - cell1.getLy());
    }

    public static MapCell[][] getVisao(Entidade e) {
        MapCell[][] visao = e.getVisao();
        int raio = e.getRaioVisao();
        int lx0 = e.getLx() - raio;
        int lx1 = e.getLx() + raio;
        int ly0 = e.getLy() - raio;
        int ly1 = e.getLy() + raio;
        for (int i = lx0, x = 0; i <= lx1; i++, x++) {
            for (int j = ly0, y = 0; j <= ly1; j++, y++) {
                visao[x][y] = mapLimitsOk(i, j) ? getCell(i, j) : null;
            }
        }
        return visao;
    }

    private static boolean mapLimitsOk(int lx, int ly) {
        return lx >= 0 && lx < Const.TOTAL_TILE_X && ly >= 0 && ly < Const.TOTAL_TILE_Y;
    }

    public static void addEntidade(Entidade e) {
        entidades.add(e);
    }

    private static int distComp(MapCell cell0, Entidade e) {
        int d = Integer.MAX_VALUE;
        for (MapCell[] x : e.getCurrentCells()) {
            for (MapCell cell1 : x) {
                int dist = getDistancia(cell0, cell1);
                if (dist < d) {
                    d = dist;
                }
            }
        }
        return d;
    }

    public static boolean podeAtacar(Entidade e0, Entidade e1) {
        boolean e0multipla = e0.getPropriedades().isBaseMultipla();
        boolean e1multipla = e1.getPropriedades().isBaseMultipla();
        int d = Integer.MAX_VALUE;
        if (e0multipla ^ e1multipla) {
            MapCell cell = e0multipla ? e1.getCurrentCell() : e0.getCurrentCell();
            Entidade e = e0multipla ? e0 : e1;
            d = distComp(cell, e);
        } else if (e0multipla && e1multipla) {
            for (MapCell[] x : e0.getCurrentCells()) {
                for (MapCell e0cell : x) {
                    int dist = distComp(e0cell, e1);
                    if (dist < d) {
                        d = dist;
                    }
                }
            }

        } else {
            d = getDistancia(e0.getCurrentCell(), e1.getCurrentCell());
        }
        return (d == 1) || (d == 2 && e0.getLx() != e1.getLx() && e0.getLy() != e1.getLy());
    }

    public static int viradoPara(Entidade queAtaca, Entidade atacada) {
        Pos posRef = getPosicaoReferencia(queAtaca, getLimitesDaEntidade(atacada));
        int refH = posRef.getX();
        int refV = posRef.getY();

        if (refV == 0) {
            return 0;
        } else if (refV == 2) {
            return 2;
        } else if (refH == 1) {
            return 1;
        } else {
            return 3;
        }
    }

    public static void moveMapa(int deltax, int deltay) {
        int novoX = getDx() + deltax;
        int novoY = getDy() + deltay;
        
        if (novoX<=0 && novoY<=Const.MENUSUP_ALTURA) { //Verificação dos limites de deslocamento do mapa
            deslocamentoMapa.setX(novoX);
            deslocamentoMapa.setY(novoY);
            for (MapCell[] m : map) {
                for (MapCell c : m) {
                    c.updatePos();
                }
            }
            for (Entidade e : entidades) {
                e.setCenterX(e.getCenterX() + deltax);
                e.getGerenteMov().setX1(e.getGerenteMov().getX1() + deltax);
                e.setCenterY(e.getCenterY() + deltay);
                e.getGerenteMov().setY1(e.getGerenteMov().getY1() + deltay);
            }
        }

    }

    public static MapCell getCell2(int x, int y) {
        return getCell(getTileLx(x),getTileLy(y));
    }
}
