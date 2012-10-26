package entidades;

import util.Dimensao;
import util.Pos;

public enum Propriedades {

    CASTELO(Construcao.class, new Dimensao(96, 96), new Dimensao(3, 2), 0, 0, 50, 1, 1),
    CATAPULTA(Construcao.class, new Dimensao(29, 21), new Dimensao(1, 1), 0, 0, 50, 1, 1),
    TORRE(Construcao.class, new Dimensao(40, 64), new Dimensao(1, 1), 0, 0, 50, 2, 1),
    BARRACKS(Construcao.class, new Dimensao(72, 42), new Dimensao(2, 1), 0, 0, 50, 1, 1),
    HUMANO(Humano.class, new Dimensao(12, 26), new Dimensao(1, 1), 5, 2, 20, -1, -1),
    ARVORE(EntidadeNeutra.class, new Dimensao(32, 32), new Dimensao(1, 1), 0, 0, 0, -1, -1);
    private Class tipo;
    private Dimensao tamanho;
    private Dimensao tamanhoBase;
    private int velocidade;
    private int raioVisao;
    private int hp;
    private int nConstSprites;
    private int nDetonSprites;

    Propriedades(Class tipo, Dimensao tamanho, Dimensao tamanhoBase, int velocidade, int raioVisao, int hp, int nConstSprites, int nDetonSprites) {
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.tamanhoBase = tamanhoBase;
        this.velocidade = velocidade;
        this.raioVisao = raioVisao;
        this.hp = hp;
        this.nConstSprites = nConstSprites;
        this.nDetonSprites = nDetonSprites;
    }

    public Class getTipo() {
        return tipo;
    }

    public Dimensao getTamanho() {
        return tamanho;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getRaioVisao() {
        return raioVisao;
    }

    public int getHp() {
        return hp;
    }

    public String getDirSpriteOriginal() {
        return "res/" + this.name().toLowerCase() + "/" + this.name().toLowerCase() + ".png";
    }

    public String getDirSpriteDetonado() {
        return "res/" + this.name().toLowerCase() + "/detonado.png";
    }

    public String getDirSpriteConstrucao() {
        return "res/" + this.name().toLowerCase() + "/construcao.png";
    }

    public Dimensao getTamanhoBase() {
        return tamanhoBase;
    }

    public boolean isImagemMaiorQueBase() {
        return (getAltura() > getBaseAltura() * 32) || (getLargura() > getBaseLargura() * 32);
    }

    public boolean isBaseMultipla() {
        return (getBaseAltura() > 1 || getBaseLargura() > 1);
    }

    public int getAltura() {
        return tamanho.getAltura();
    }

    public int getLargura() {
        return tamanho.getLargura();
    }

    public int getBaseAltura() {
        return tamanhoBase.getAltura();
    }

    public int getBaseLargura() {
        return tamanhoBase.getLargura();
    }

    public int getnConstSprites() {
        return nConstSprites;
    }

    public int getnDetonSprites() {
        return nDetonSprites;
    }
}
