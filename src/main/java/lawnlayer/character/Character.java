package lawnlayer.character;

import processing.core.PImage;

import java.util.Random;

import lawnlayer.App;
import processing.core.PApplet;

/**
 * Represents an abstract Character object that is inherited by Player and Enemy.
 */
public abstract class Character {
    
    /**
     * The row of the Character.
     */
    protected int row;

    /**
     * The column of the Character.
     */
    protected int col;
    
    /**
     * The x-coordinate of the Character.
     */
    protected int x;
    
    /**
     * The y-coordinate of the Character.
     */
    protected int y;

    /**
     * The speed of the Character.
     */
    protected int speed;

    /**
     * The sprite of the Character.
     */
    private PImage sprite;

    /**
     * Create a new Character object at a random empty tile.
     * 
     * @param tile A 2-dimension <code>int</code> array. Represent the type of tile of all the grids.
     */
    public Character(int[][] tile) {
        // Generate random position
        Random rd = new Random();
        do {
            this.row = rd.nextInt(tile.length-2) + 1; // 1 ~ number of row - 1
            this.col = rd.nextInt(tile[0].length-2) + 1; // 1 ~ number of column - 1
        } while(tile[this.getRow()][this.getCol()] != 0); // Regerenate the position if the tiles is not empty
        this.y = row * App.SPRITESIZE + App.TOPBAR;
        this.x = col * App.SPRITESIZE;
        this.speed = 2;
    }

    /**
     * Creates a new Character object at the given position.
     * 
     * @param row The row of the Character
     * @param col The column of the Character
     */
    public Character(int row, int col) {
        this.row = row;
        this.col = col;
        this.y = row * App.SPRITESIZE + App.TOPBAR;
        this.x = col * App.SPRITESIZE;
        this.speed = 2;
    }

    /**
     * Sets the Character's sprite.
     * 
     * @param sprite The new sprite to use.
     */
    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Update the position of the <code>Worm</code> object every frame
     * 
     * @param app The window to draw onto.
     */
    public abstract void tick(App app);

    /**
     * Draws the Character to the screen.
     * 
     * @param app The window to draw onto.
     */
    public void draw(App app) {
        // The image() method is used to draw PImages onto the screen.
        // The first argument is the image, the second and third arguments are x and y coordinates of the image
        app.image(this.sprite, this.x, this.y);
    }

    /**
     * Gets the x-coordinate.
     * @return The x-coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate.
     * @return The y-coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Update the row and the column of the Character base on its coordinate.
     */
    public void updateGrid() {
        this.row = PApplet.floor((this.y - App.TOPBAR) / App.SPRITESIZE);
        this.col = PApplet.floor(this.x / App.SPRITESIZE);
    }

    /**
     * 
     * @return the row of the Character
     */
    public int getRow() {
        return this.row;
    }
    
    /**
     * 
     * @return the column of the Caracter
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Set the row of the Character
     * @param r row
     */
    public void setRow(int r) {
        this.row = r;
    }
    
    /**
     * Set the column of the Character
     * @param c column
     */
    public void setCol(int c) {
        this.col = c;
    }

    /**
     * Set the speed of the Character
     * @param newSpeed new speed
     */
    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    /**
     * 
     * @return get the speed of the Character
     */
    public int getSpeed() {
        return this.speed;
    }
}