package util;

import util.Util;
import entidades.Entidade;
import java.util.ArrayList;
import gerentes.Mapa;

public class AStar {

    No[][] grid;
    ArrayList<No> listaAberta;
    ArrayList<No> listaFechada;
    Entidade entidade;

    public AStar(Entidade entidade) {
        this.entidade = entidade;
        grid = new No[Const.TOTAL_TILE_X][Const.TOTAL_TILE_Y];
        for (int lx = 0; lx < Const.TOTAL_TILE_X; lx++) {
            for (int ly = 0; ly < Const.TOTAL_TILE_Y; ly++) {
                grid[lx][ly] = new No(Mapa.getMap()[lx][ly]);
            }
        }
    }

    public ArrayList<No> getAdjacentes(No no) {
        ArrayList<No> adj = new ArrayList<>();
        No esq = null, cima = null, dir = null, baixo = null;
        int lx = no.lx, ly = no.ly;
        if (lx - 1 >= 0) {
            esq = grid[lx - 1][ly];
        }
        if (ly - 1 >= 0) {
            cima = grid[lx][ly - 1];
        }
        if (lx + 1 < grid.length) {
            dir = grid[lx + 1][ly];
        }
        if (ly + 1 < grid[0].length) {
            baixo = grid[lx][ly + 1];
        }
        if (esq != null) {
            adj.add(esq);
        }
        if (cima != null) {
            adj.add(cima);
        }
        if (dir != null) {
            adj.add(dir);
        }
        if (baixo != null) {
            adj.add(baixo);
        }
        return adj;
    }

    public ArrayList<No> geraMelhorCaminho(int i0, int j0, int i1, int j1) {
        ArrayList<No> melhorCaminho = new ArrayList();
        No noFinal = grid[i1][j1];
        if (noFinal.getCell().isCaminhavel(entidade)) {
            listaAberta = new ArrayList();
            listaFechada = new ArrayList();
            limpaPais();
            No noInicial = grid[i0][j0];
            noInicial.g = 0;
            setaHeuristica(noFinal);
            No atual = null;
            listaAberta.add(noInicial);
            while (!listaAberta.isEmpty()) {
                atual = procuraNoComMenorF();
                listaAberta.remove(atual);
                listaFechada.add(atual);
                if (atual.equals(noFinal)) {
                    break;
                }
                ArrayList<No> nosAdjacentes = getAdjacentes(atual);
                for (No no : nosAdjacentes) {
                    if (no.getCell().isCaminhavel(entidade) && !listaFechada.contains(no)) {
                        if (listaAberta.contains(no)) {
                            if (no.g > atual.g + 1) {
                                no.setPai(atual);
                            }
                        } else {
                            listaAberta.add(no);
                            no.setPai(atual);
                        }
                    }
                }
            }
            //Gera arraylist de melhor caminho
            while (atual.pai != null) {
                melhorCaminho.add(atual);
                atual = atual.pai;
            }
        }
        return melhorCaminho;
    }

    private No procuraNoComMenorF() {
        No noMenorF = null;
        int menorF = 100000;
        for (No no : listaAberta) {
            if (no.getF() < menorF || noMenorF == null) {
                noMenorF = no;
                menorF = noMenorF.getF();
            }
        }
        return noMenorF;
    }

    private void setaHeuristica(No noFinal) {
        for (No[] no0 : grid) {
            for (No no : no0) {
                no.setaHeuristica(noFinal);
            }
        }
    }

    private void limpaPais() {
        for (No[] no0 : grid) {
            for (No no : no0) {
                no.pai = null;
            }
        }
    }
}
