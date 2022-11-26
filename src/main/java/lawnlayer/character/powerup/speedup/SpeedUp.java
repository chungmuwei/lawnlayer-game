package lawnlayer.character.powerup.speedup;

import lawnlayer.App;
import lawnlayer.character.powerup.Powerup;

public class SpeedUp extends Powerup {

    /**
     * boosted speed of the player
     */
    public static final int BOOSTEDSPEED = 4;
    /**
     * normal speed of the player
     */
    public static final int NORMALSPEED = 2;

    /**
     * Constructor for SpeedUp
     * @param tile Represent the type of tile of all the grids
     */
    public SpeedUp(int[][] tile) {
        super(tile);
    }

    /**
     * Update the attribute of the <code>PowerUp</code>
     * @param app The window to draw onto.
     */
    @Override
    public void tick(App app) {
        // System.out.println(this.remainingFramesToDraw + ", " + this.remainingEffectFrames);

        if (app.tile[this.getRow()][this.getCol()] != 0) {
            this.respawn(app.tile);
        }

        if (this.remainingFramesToDraw > 0) {
            this.remainingFramesToDraw--;
        
        } else if (this.remainingFramesToDraw == 0 && this.remainingEffectFrames > 0) {
            this.checkCollisionWithPlayer(app);
        } 

        if (this.startEffect && this.remainingEffectFrames > 0) {
            this.remainingEffectFrames--;
        
        } else if (this.remainingEffectFrames == 0) {
            this.endEffect(app);
            this.remainingEffectFrames = EFFECTFRAMES;
            this.remainingFramesToDraw = DELAYFRAMES;
            this.startEffect = false;
        }

    }

    @Override
    public void effect(App app) {
        app.player.setSpeed(BOOSTEDSPEED);
    }
    
    @Override
    public void endEffect(App app) {
        app.player.setSpeed(NORMALSPEED);
        this.startEffect = false;
    }

}