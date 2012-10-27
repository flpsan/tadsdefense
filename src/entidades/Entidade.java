package entidades;

import gerentes.GerenteAnimacao;
import gerentes.GerenteBatalha;
import gerentes.GMapa;
import gerentes.GerenteMovimento;
import org.newdawn.slick.geom.Rectangle;
import tadsdefense.MapCell;

public abstract class Entidade {

    private GerenteAnimacao gerenteAnimacao;
    private GerenteBatalha gerenteBatalha;
    private GerenteMovimento gerenteMov;
    private MapCell currentCell;
    private MapCell[][] currentCells;
    private boolean dead;
    private boolean movel;
    private Time time;
    private int hpMax;
    private int hp;
    private Rectangle hpBox;
    private Rectangle hpBoxFundo;
    private Rectangle box;
    private boolean colidivel;
    private String nome;
    private int cellPos;
    private Propriedades propriedades;

    public Entidade(Propriedades propriedades, int lx, int ly, boolean colidivel, Time time, boolean movel) {
        this.propriedades = propriedades;
        this.dead = false;
        this.time = time;
        this.gerenteMov = new GerenteMovimento(this);
        this.movel = movel;
        this.colidivel = colidivel;
        this.gerenteBatalha = new GerenteBatalha(this, 3, propriedades.getRaioVisao());
        if (propriedades.isImagemMaiorQueBase()) {
            this.box = new Rectangle(0, 0, propriedades.getBaseLargura() * 32, propriedades.getBaseAltura() * 32);
        } else {
            this.box = new Rectangle(0, 0, propriedades.getTamanho().getLargura(), propriedades.getTamanho().getAltura());
        }
        this.hpBox = new Rectangle(0, 0, 0, 3);
        this.hpBoxFundo = new Rectangle(0, 0, 20, 5);
        this.hpMax = propriedades.getHp();
        this.hp = (propriedades.getTipo().equals(Construcao.class))?1:propriedades.getHp();
        defaultInit(lx, ly);
    }

    private void defaultInit(int lx, int ly) {
        updateHpBoxWidth();
        setCurrentCell(GMapa.getCell(lx, ly));
        if (propriedades.isBaseMultipla()) {
            int baseL = propriedades.getBaseLargura();
            int baseA = propriedades.getBaseAltura();
            currentCells = new MapCell[baseL][baseA];
            for (int i = 0; i < baseL; i++) {
                for (int j = 0; j < baseA; j++) {
                    currentCells[i][j] = GMapa.getCell(lx + i, ly + j);
                    getCurrentCells()[i][j].addEntidade(this);
                }
            }
            setCenterX((int) currentCell.getBox().getX() + (baseL * 32 / 2));
            setCenterY((int) currentCell.getBox().getY() + (baseA * 32 / 2));
        } else {
            setCenterX((int) getCurrentCell().getBox().getCenterX());
            setCenterY((int) getCurrentCell().getBox().getCenterY());
        }
    }

    public abstract void draw();

    public void goTo(int tileLx, int tileLy) {
        gerenteMov.startPathfind(tileLx, tileLy);
    }

    public void goTo(MapCell cell) {
        gerenteMov.startPathfind(cell.getLx(), cell.getLy());
    }

    public void setCurrentCell(MapCell cell) {
        if (getCurrentCell() != null) {
            getCurrentCell().removeEntidade(this);
        }
        currentCell = cell;
        getCurrentCell().addEntidade(this);
        getGerenteBatalha().setVisao(GMapa.getVisao(this));
    }

    public void update() {
        getGerenteMov().update();
    }

    public boolean isColidivel() {
        return colidivel;
    }

    public int getX() {
        return (int) getBox().getX();
    }

    public int getCenterX() {
        return (int) getBox().getCenterX();
    }

    public int getY() {
        return (int) getBox().getY();
    }

    public int getCenterY() {
        return (int) getBox().getCenterY();
    }

    public int getLx() {
        return getCurrentCell().getLx();
    }

    public int getLy() {
        return getCurrentCell().getLy();
    }

    public void setColidivel(boolean colidivel) {
        this.colidivel = colidivel;
    }

    public int getLargura() {
        return (int) getBox().getWidth();
    }

    public void setLargura(int largura) {
        getBox().setWidth(largura);
    }

    public int getAltura() {
        return (int) getBox().getHeight();
    }

    public void setAltura(int altura) {
        getBox().setHeight(altura);
    }

    public GerenteMovimento getGerenteMov() {
        return gerenteMov;
    }

    public Rectangle getBox() {
        return box;
    }

    public void setCenterX(int x) {
        getBox().setCenterX(x);
        getHpBoxFundo().setCenterX(getBox().getCenterX());
        getHpBox().setX(getHpBoxFundo().getX() + 1);
    }

    public void setCenterY(int y) {
        getBox().setCenterY(y);
        getHpBoxFundo().setCenterY(getBox().getCenterY() - getBox().getHeight() / 2 - getHpBox().getHeight());
        getHpBox().setCenterY(getHpBoxFundo().getCenterY());
    }

    public MapCell getCurrentCell() {
        return currentCell;
    }

    public void setGerenteMov(GerenteMovimento gerenteMov) {
        this.gerenteMov = gerenteMov;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCellPos() {
        return cellPos;
    }

    public void setCellPos(int cellPos) {
        this.cellPos = cellPos;
    }

    public Rectangle getHpBoxFundo() {
        return hpBoxFundo;
    }

    public void setHpBoxFundo(Rectangle hpBoxFundo) {
        this.hpBoxFundo = hpBoxFundo;
    }

    public int getHp() {
        return hp;
    }

    public void hit(int hit) {
        setHp(getHp() - hit);
    }

    /*
     * Avisa ao mapa e a quem está atacando que morreu
     */
    public void morri() {
        for (Entidade e : getGerenteBatalha().getAtacadoPor()) {
            e.desataca();
        }
        getCurrentCell().removeEntidade(this);
        if (propriedades.isBaseMultipla()) {
            for(MapCell[] x : currentCells){
                for(MapCell cell : x){
                    cell.removeEntidade(this);
                }
            }
        }
    }

    public void setHp(int hp) {
        if (hp <= 0) {
            hp = 0;
            setDead(true);
        } else if (hp>hpMax){
            hp = hpMax;
        }
        this.hp = hp;
        updateHpBoxWidth();
    }

    private void updateHpBoxWidth() {
        getHpBox().setWidth(((hp / (float) hpMax) * (getHpBoxFundo().getWidth() - 2)));
    }

    public Rectangle getHpBox() {
        return hpBox;
    }

    public void setHpBox(Rectangle hpBox) {
        this.hpBox = hpBox;
    }

    public int getHpMax() {
        return hpMax;
    }

    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }

    public GerenteBatalha getGerenteBatalha() {
        return gerenteBatalha;
    }

    protected void setGerenteBatalha(GerenteBatalha gerenteBatalha) {
        this.gerenteBatalha = gerenteBatalha;
    }

    public void removeAtacadoPor(Entidade h) {
        getGerenteBatalha().removeAtacadoPor(h);
    }

    public void addAtacadoPor(Entidade h) {
        getGerenteBatalha().addAtacadoPor(h);

    }

    public void ataca(Entidade e) {
        getGerenteBatalha().ataca(e);
    }

    public boolean isMovel() {
        return movel;
    }

    public void setMovel(boolean movel) {
        this.movel = movel;
    }

    public boolean isInimigoDe(Entidade entidade) {
        return entidade.getTime() != this.getTime();
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public MapCell[][] getVisao() {
        return getGerenteBatalha().getVisao();
    }

    public int getRaioVisao() {
        return getGerenteBatalha().getRaioVisao();
    }

    public void desataca() {
        getGerenteBatalha().desataca();
    }

    public Time getTime() {
        return time;
    }

    public Propriedades getPropriedades() {
        return propriedades;
    }

    public MapCell[][] getCurrentCells() {
        return currentCells;
    }
    
    public double getHpPercent(){
        return (double)getHp()/(double)getHpMax();
    }

    public GerenteAnimacao getGerenteAnimacao() {
        return gerenteAnimacao;
    }

    public void setGerenteAnimacao(GerenteAnimacao gerenteAnimacao) {
        this.gerenteAnimacao = gerenteAnimacao;
    }
}
