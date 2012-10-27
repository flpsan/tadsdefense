package gerentes;

import entidades.Construcao;
import entidades.Humano;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import util.Const;

public class Menu {

    Rectangle menuSup;
    Rectangle menuInf;
    Selecao selecao;
    
    static final int E = 10;
    
    public Menu(Selecao selecao){
        this.menuSup = new Rectangle(0, 0, Const.LARGURA_TELA + 1, Const.MENUSUP_ALTURA);
        this.menuInf = new Rectangle(0, Const.ALTURA_TELA - Const.MENUINF_ALTURA, Const.LARGURA_TELA + 1, Const.MENUINF_ALTURA+1);
        this.selecao = selecao;
    }


    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fill(menuSup);
        g.fill(menuInf);
        String a = "";
        if (selecao.isHumanoSelecionado()){
            Humano h = selecao.getHumanoSelecionado();
            a = "hp: "+h.getHp()+"/"+h.getHpMax();
        } else if (selecao.isConstrucaoSelecionada()){
            Construcao c = selecao.getConstrucaoSelecionada();
            a = c.getPropriedades().name()+" "+(c.isConstruido()?"construído":"não construído");
        } else if (selecao.isHumanosSelecionados()){
            a = "humanosss";
        }
        g.setColor(Color.white);
        g.drawString(a, menuSup.getX()+E, menuSup.getY()+E);
    }
}
