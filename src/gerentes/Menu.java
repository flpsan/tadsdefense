package gerentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import util.Const;

public class Menu {

    Rectangle menuSup;
    Rectangle menuInf;
    
    public Menu(GameContainer gc){
        menuSup = new Rectangle(0, 0, Const.LARGURA_TELA + 1, Const.MENUSUP_ALTURA);
        menuInf = new Rectangle(0, Const.ALTURA_TELA - Const.MENUINF_ALTURA, Const.LARGURA_TELA + 1, Const.MENUINF_ALTURA+1);
    }


    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fill(menuSup);
        g.fill(menuInf);
    }
}
