package gerentes;

import org.newdawn.slick.Animation;

public class GerenteAnimacao {

    private int currentAnim = 0;
    private Animation[] anims;
    private Animation hitAnim;

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

    public void playAtaca(int ladoAlvo) {
        if (!isAtacando()) {
            currentAnim = 4+ladoAlvo;
        }
    }

    public boolean isAtacando() {
        return currentAnim >= 4;
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

    public void playHitAnim() {
        if (getHitAnim().isStopped()){
            getHitAnim().restart();
        }
    }


    public Animation getHitAnim() {
        return hitAnim;
    }

    public void setHitAnim(Animation hitAnim) {
        this.hitAnim = hitAnim;
        hitAnim.setLooping(false);
        hitAnim.stop();
    }
}
