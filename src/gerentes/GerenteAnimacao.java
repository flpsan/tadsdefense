package gerentes;

import org.newdawn.slick.Animation;

public class GerenteAnimacao {

    private int currentAnim = 0;
    private Animation[] anims;

    public GerenteAnimacao(Animation[] anims) {
        this.anims = anims;
    }

    public void troca(int indice) {
        currentAnim = indice;
    }

    public void stop() {
        anims[currentAnim].stop();
    }

    public void play() {
        anims[currentAnim].start();
    }

    public Animation getAnimation() {
        return anims[currentAnim];
    }

    public void playAtaca() {
        if (!isAtacando()) {
            currentAnim = 4;
        }
    }

    public boolean isAtacando() {
        return currentAnim == 4;
    }

    public void stopAtaca() {
        if (isAtacando()) {
            currentAnim = 0;
            stop();
        }
    }

    public int getIndice() {
        return currentAnim;
    }
}
