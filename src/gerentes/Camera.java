package gerentes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import util.Const;

public class Camera {

    Rectangle camDeslocSup;
    Rectangle camDeslocDir;
    Rectangle camDeslocInf;
    Rectangle camDeslocEsq;

    public Camera(GameContainer gc) {
        int d = Const.CAMERA_DESLOC_ALT_LARG;
        int screenW = gc.getWidth();
        int screenH = gc.getHeight();
        int altTelaJogo = screenH - Const.MENUINF_ALTURA - Const.MENUSUP_ALTURA;
        int yInf = screenH - Const.MENUINF_ALTURA;
        camDeslocSup = new Rectangle(0, Const.MENUSUP_ALTURA, screenW, d);
        camDeslocDir = new Rectangle(screenW - d, Const.MENUSUP_ALTURA + d, d, altTelaJogo - d);
        camDeslocInf = new Rectangle(0, yInf, screenW, d);
        camDeslocEsq = new Rectangle(0, Const.MENUSUP_ALTURA + d, d, altTelaJogo - d);
    }

    public void update(int x, int y) {
        int velCamera = 10;
        if (camDeslocSup.contains(x, y)) {
            Mapa.moveMapa(0, velCamera);
        } else if (camDeslocDir.contains(x, y)) {
            Mapa.moveMapa(-velCamera, 0);
        } else if (camDeslocInf.contains(x, y)) {
            Mapa.moveMapa(0, -velCamera);
        } else if (camDeslocEsq.contains(x, y)) {
            Mapa.moveMapa(velCamera, 0);
        }
    }
}
