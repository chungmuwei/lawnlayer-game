package lawnlayer.character.enemy;

import java.util.Random;

import lawnlayer.character.Character;

public abstract class Enemy extends Character {

    /**
     * Whether to move left
     */
    protected boolean moveLeft;

    /**
     * Whether to move up
     */
    protected boolean moveUp;

    /**
     * Create an <code>Enemy</code> object at a random empty tile with random initial direction.
     * 
     * @param tile A 2-dimension <code>int</code> array. Represent the type of tile of all the grids.
     */
    public Enemy(int[][] tile) {
        super(tile);
        // Generate random direction
        Random rd = new Random();
        this.moveLeft = rd.nextBoolean();
        this.moveUp = rd.nextBoolean();
    }
    
    /**
     * Create an <code>Enemy</code> object at the given empty tile with random initial direction.
     * 
     * @param type
     * @param initialRow
     * @param initialCol
     */
    public Enemy(int initialRow, int initialCol) {
        super(initialRow, initialCol);
        // Generate random direction
        Random rd = new Random();
        this.moveLeft = rd.nextBoolean();
        this.moveUp = rd.nextBoolean();
    }

    /**
     * Getter for <code>moveLeft</code> attribute
     * @return whether the <code>Enemy</code> moves left
     */
    public boolean isMoveLeft() {
        return this.moveLeft;
    }

    /**
    * Getter for <code>moveUp</code> attribute
    * @return whether the <code>Enemy</code> moves up
    */
    public boolean isMoveUp() {
        return this.moveUp;
    }
}