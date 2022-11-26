package lawnlayer.character.powerup;

import java.util.Random;

import lawnlayer.App;
import lawnlayer.character.Character;

public abstract class Powerup extends Character {
    
    /**
     * the number of frames delay to show the powerup
     */
    public static final int DELAYFRAMES = 10 * App.FPS;

    /**
     * the number of frames of the effect
     */
    public static final int EFFECTFRAMES = 10 * App.FPS;

    /**
     * the remaining frames to show the powerup
     */
    protected int remainingFramesToDraw;

    /**
     * the remaining frames of the effect
     */
    protected int remainingEffectFrames;

    /**
     * whether the effect started
     */
    protected boolean startEffect;

    /**
     * Create a new Character object at a random empty tile.
     * 
     * @param tile A 2-dimension <code>int</code> array. Represent the type of tile of all the grids.
     */
    public Powerup(int[][] tile) {
        super(tile);
        this.remainingFramesToDraw = DELAYFRAMES;
        this.remainingEffectFrames = EFFECTFRAMES;
        
    }

    /**
     * Start the effect
     * @param app The window to draw onto.
     */
    public abstract void effect(App app);
    
    /**
     * End the effect
     * @param app The window to draw onto.
     */
    public abstract void endEffect(App app);

    /**
     * Check collision with player
     * @param app The window to draw onto.
     */
    public void checkCollisionWithPlayer(App app) {
    
        if (this.getRow() == app.player.getRow() && this.getCol() == app.player.getCol()) {
            this.startEffect = true;
            this.effect(app);
            this.respawn(app.tile);
        }
  
    }

    /**
     * Respawn the powerup
     * @param tile Represent the type of tile of all the grids
     */
    public void respawn(int[][] tile) {
        this.remainingFramesToDraw = DELAYFRAMES;
        // this.remainingEffectFrames = EFFECTFRAMES;
        // Generate random position
        Random rd = new Random();
        do {
            this.row = rd.nextInt(tile.length-2) + 1; // 1 ~ number of row - 1
            this.col = rd.nextInt(tile[0].length-2) + 1; // 1 ~ number of column - 1
        } while(tile[this.getRow()][this.getCol()] != 0); // Regerenate the position if the tiles is not empty
        this.y = row * App.SPRITESIZE + App.TOPBAR;
        this.x = col * App.SPRITESIZE;
    }

    /**
     * Get remaining frames to show the powerup
     * @return <code>remainingFramesToDraw</code>
     */
    public int getRemainingFramesToDraw() {
        return this.remainingFramesToDraw;
    }

    /**
     * Get remaining frames of the effect
     * @return <code>remainingEffectFrames</code>
     */
    public int getRemainingEffectFrames() {
        return this.remainingEffectFrames;
    }

    /**
     * Return whether the effect has started 
     * @return <code>startEffect</code>
     */
    public boolean hasStartedEffect() {
        return this.startEffect;
    }

}
