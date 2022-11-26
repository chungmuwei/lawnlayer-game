package lawnlayer.character.player;
import java.util.ArrayList;
import java.util.List;

import lawnlayer.App;
import lawnlayer.character.Character;

/**
 * Represents a player
 */
public class Player extends Character {

    public static final int INITIALROW = 0;
    public static final int INITIALCOL = 0;

    /**
     * The remaining lives of the Player
     */
    private int remainingLives;

    /**
     * An integer specifies the current direction of the player
     * 0: stop, 37: left, 38: up, 39: right, 40: down
     */
    private int direction;

    /**
     * An integer specifies the upcoming direction of the player. Player is only allowed to move 
     * within the tile. If Player changes its direction between two tiles, it must store the next 
     * direction and wait until it moves onto the next tile to change its direction 
     */
    private int nextDireciton;

    /**
     * Count the remaining frames to change direction 
     */
    private int remainingFrames;

    /**
     * A list of integer array which stores the row and column of the tiles of the Player's path
     */
    private List<int[]> path;


    /**
     * Create a player at initial position
     * 
     * @param lives The total lives of the <code>Player</code>
     */
    public Player(int lives) {
        super(INITIALROW, INITIALCOL);
        this.remainingLives = lives;
        this.remainingFrames = 0;
        this.path = new ArrayList<int[]>();
    }

    /**
     * Update the position of the <code>Player</code> object every frame 
     * 
     * @param app The window to draw onto.
     */
    @Override
    public void tick(App app) {

        // On concrete or gras tiles, the Player stops when if arrow key is released
        if (app.keyIsReleased && 
        (app.tile[this.getRow()][this.getCol()] == 1 || app.tile[this.getRow()][this.getCol()] == 4)) {
            this.setDireciton(0);
        }

        // If Player wants to change direction, calculate the remaining frames to change direction
        if (this.nextDireciton != this.direction) {
            
            // Is moving left
            if (this.direction == 37) {
                this.remainingFrames = this.getX() % App.SPRITESIZE;
            // Is moving right
            } else if (this.direction == 39) {
                this.remainingFrames = (App.SPRITESIZE - this.getX() % App.SPRITESIZE) % App.SPRITESIZE;
            // Is moving up
            } else if (this.direction == 38) {
                this.remainingFrames = this.getY() % App.SPRITESIZE;
            // Is moving down 
            } else if (this.direction == 40) {
                this.remainingFrames = (App.SPRITESIZE - this.getY() % App.SPRITESIZE) % App.SPRITESIZE;
            }   
        }

        // Change direction only if the player is onto a tile
        if (this.remainingFrames == 0) {
            this.direction = this.nextDireciton;
        }

        // Update the coordinate of the player base on the direction
        // Left
        if (this.direction == 37) {
            this.x = (int) Math.max(this.x - this.speed, 0);
        // Right
        } else if (this.direction == 39) {
            this.x = (int) Math.min(this.x + this.speed, App.WIDTH - App.SPRITESIZE);
        // Up
        } else if (this.direction == 38) {
            this.y = (int) Math.max(this.y - this.speed, App.TOPBAR);
        // Down
        } else if (this.direction == 40) {
            this.y = (int) Math.min(this.y + this.speed, App.HEIGHT - App.SPRITESIZE);
        }

        // Update the row and column of the player
        this.updateGrid();
    }

    /**
     * Called in <code>App</code> when direction of the <code>Player</code> is going to change.
     * 
     * @param dir An integer which specifies the current direction of the player.
     */
    public void setDireciton(int dir) {
        this.nextDireciton = dir;
    }

    /**
     * Get the current direction of the <code>Player</code>
     * 
     * @return the current direciton of the <code>Player</code>.
     */
    public int getDirection() {
        return this.direction;
    }

    /**
     * Get the remaining lives of the <code>Player</code>
     * @return the remaining lives of the <code>Player</code>
     */
    public int getRemainingLives() {
        return this.remainingLives;
    }

    /**
     * Get the <code>path</code> of the <code>Player</code>
     * @return the <code>path</code> of the <code>Player</code>
     */
    public List<int[]> getPath() {
        return this.path;
    }

    /**
     * Get the first element of the <code>path</code>.
     * @return If <code>path</code> is not empty, return the first element. Otherwise, return <code>null</code>
     */
    public int[] getFirstTileInPath() {
        if (this.path.size() == 0) {
            return null;
        }
        return this.path.get(0);
    }  
    
    /**
     * Get the last element of the <code>path</code>.
     * @return If <code>path</code> is not empty, return the last element. Otherwise, return <code>null</code>
     */
    public int[] getLastTileInPath() {
        if (this.path.size() == 0) {
            return null;
        }
        return this.path.get(this.path.size()-1);
    }    

    /**
     * Add an element at the end of the <code>path</code>
     * @param vertex A <code>int[]</code> of length 2 which represents the row and the column
     */
    public void addVertexToPath(int[] vertex) {
        this.path.add(vertex);  
    }

    /**
     * Empty the <code>path</code>
     */
    public void removePath() {
        this.path.clear();
    }


    /**
     * Called when the player is dead. 
     * <p>
     * Respawn <Player> at the top left corner, decrease remaining lives by 1, and stay stationary.
     * <p>
     * Clear <path> list and change all the path tiles back to dirt tiles. 
     * @param tile A 2-dimension <code>int</code> array. Represent the type of tile of all the grids.
     */
    public void respawn(int[][] tile) {
        this.row = INITIALROW;
        this.col = INITIALCOL;
        this.y = row * App.SPRITESIZE + App.TOPBAR;
        this.x = col * App.SPRITESIZE;
        this.setDireciton(0);
        this.remainingLives--;
        this.removePath();
        // change all the path tiles back to dirt tiles
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[0].length; j++) {
                if (tile[i][j] == 2 || tile[i][j] == 3) {
                    tile[i][j] = 0;
                }
            }
        }
    }
}
