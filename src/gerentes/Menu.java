package gerentes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import util.Const;

public class Menu {

    Rectangle menuSup;
    Rectangle menuInf;
    
    public Menu(GameContainer gc){
        menuSup = new Rectangle(0, 0, gc.getWidth(), Const.MENUSUP_ALTURA);
        menuInf = new Rectangle(0, gc.getHeight() - Const.MENUINF_ALTURA + Const.CAMERA_DESLOC_ALT_LARG, gc.getWidth(), Const.MENUINF_ALTURA);
    }
}
