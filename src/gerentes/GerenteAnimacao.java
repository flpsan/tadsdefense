/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public void pausa() {
        anims[currentAnim].stop();
    }

    public void play() {
        anims[currentAnim].start();
    }
    
    public Animation getAnimation(){
        return anims[currentAnim];
    }
}
