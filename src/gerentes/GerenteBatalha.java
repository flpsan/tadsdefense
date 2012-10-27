package gerentes;

import entidades.Construcao;
import entidades.Entidade;
import entidades.Humano;
import entidades.Time;
import java.util.LinkedList;
import tadsdefense.MapCell;

public class GerenteBatalha {

    private static final int T_UPDATE_ENTIDADES = 30;
    private int updateCont;
    private int roundsToAttack;
    private int roundsCont;
    private Entidade entidade;
    private Entidade atacando;
    private LinkedList<Entidade> atacadoPor;
    private int raioVisao;
    private MapCell[][] visao;
    private LinkedList<Entidade> entidadesAVista;
    private boolean podeAtacar;

    public GerenteBatalha(Entidade entidade, int roundsToAttack, int raioVisao) {
        this.atacadoPor = new LinkedList();
        this.entidade = entidade;
        this.roundsToAttack = roundsToAttack;
        this.raioVisao = raioVisao;
        this.visao = new MapCell[1 + raioVisao * 2][1 + raioVisao * 2];
        this.entidadesAVista = new LinkedList();
        this.podeAtacar = false;
    }

    public void ataca(Entidade e) {
        if (e != null && e.isInimigoDe(entidade) && e.getTime() != Time.NEUTRO) {
            if (isAtacando()) {
                getAtacando().removeAtacadoPor(entidade);
            }
            e.addAtacadoPor(entidade);
            atacando = e;
        }
    }

    public void desataca() {
        if (atacando != null) {
            atacando = null;
            podeAtacar = false;
        }
    }

    public void addAtacadoPor(Entidade e) {
        if (!atacadoPor.contains(e)) {
            getAtacadoPor().add(e);
        }
    }

    public void removeAtacadoPor(Entidade e) {
        getAtacadoPor().remove(e);
    }

    public Entidade getAtacando() {
        return atacando;
    }

    public boolean isAtacando() {
        return atacando != null;
    }

    private void updateInimigosAVista() {
        getEntidadesAVista().clear();
        for (MapCell[] m : getVisao()) {
            for (MapCell c : m) {
                if (c != null) {
                    for (Entidade e : c.getEntidades()) {
                        if (e.isInimigoDe(entidade) && e.getTime() != Time.NEUTRO) {
                            getEntidadesAVista().add(e);
                        }
                    }
                }
            }
        }
    }

    public void update() {
        if (entidade.getPropriedades().podeAtacar()) {
            boolean ehConstrucao = entidade instanceof Construcao;
            if (isAtacando()) {
                podeAtacar = GerenteMapa.podeAtacar(entidade, atacando) || (ehConstrucao && entidadesAVista.contains(atacando) && ((Construcao) entidade).temCondicaoDeAtacar());
                if (podeAtacar && roundsCont++ > roundsToAttack) {
                    getAtacando().hit(1);
                    if (ehConstrucao) {
                        entidade.getGerenteAnimacao().playTiroAnim(atacando);
                    }
                    roundsCont = 0;
                } else if (entidade.getGerenteMov().getMovs().isEmpty() && !ehConstrucao) {
                    entidade.goTo(GerenteMapa.getCelulaMaisProxima(entidade, atacando));
                }
            }

            //Construções precisam atualizar pra nao acabarem atacando algo que esteja fora do raio de visão
            if (ehConstrucao) {
                updateInimigosAVista();
            }


            //ATAQUE AUTOMÁTICO - sem necessidade do jogador atacar - Time B e construcoes que atacam
            if (!isAtacando() && (entidade.getTime() == Time.B || ehConstrucao) && updateCont++ >= T_UPDATE_ENTIDADES) {
                updateInimigosAVista();
                if (!getEntidadesAVista().isEmpty()) {
                    entidade.ataca(getEntidadesAVista().getFirst());
                }
                updateCont = 0;
            }
        }
    }

    public LinkedList<Entidade> getAtacadoPor() {
        return atacadoPor;
    }

    public int getRaioVisao() {
        return raioVisao;
    }

    public void setRaioVisao(int raioVisao) {
        this.raioVisao = raioVisao;
    }

    public MapCell[][] getVisao() {
        return visao;
    }

    public void setVisao(MapCell[][] visao) {
        this.visao = visao;
        updateInimigosAVista();
    }

    public LinkedList<Entidade> getEntidadesAVista() {
        return entidadesAVista;
    }

    public void setEntidadesAVista(LinkedList<Entidade> entidadesAVista) {
        this.entidadesAVista = entidadesAVista;
    }

    public int getLadoAlvo() {
        return GerenteMapa.viradoPara(entidade, atacando);
    }

    public boolean podeAtacar() {
        return podeAtacar;
    }
}
