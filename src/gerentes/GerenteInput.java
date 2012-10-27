package gerentes;

import entidades.Entidade;
import entidades.Humano;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import tadsdefense.MapCell;

public class GerenteInput {

    Camera camera;
    Selecao selecao;
    Menu menu;

    public GerenteInput(Camera camera, Selecao selecao, Menu menu) {
        this.camera = camera;
        this.selecao = selecao;
        this.menu = menu;
    }

    public void update(GameContainer gc) {
        Input input = gc.getInput();
        int x = input.getMouseX();
        int y = input.getMouseY();
        boolean botaoEsquerdoDown = input.isMouseButtonDown(0);
        boolean botaoDireitoDown = input.isMouseButtonDown(1);
        selecao.botaoEsquerdo(botaoEsquerdoDown, x, y);
        selecao.botaoDireito(botaoDireitoDown, x, y);
        selecao.setFoco(x,y);
        camera.update(x, y);
    }
}
