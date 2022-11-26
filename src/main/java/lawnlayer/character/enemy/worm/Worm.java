package lawnlayer.character.enemy.worm;

import lawnlayer.App;
import lawnlayer.character.enemy.Enemy;

public class Worm extends Enemy{
    
    public Worm(int[][] tile) {
        super(tile);
    }

    public Worm(int initialRow, int initialCol) {
        super(initialRow, initialCol);
    }

    /**
     * Update the position of the <code>Worm</code> object every frame
     * 
     * @param app The window to draw onto.
     */
    @Override
    public void tick(App app) {
        
        if (!this.moveLeft) {
            this.col = Math.min(this.col+1, 63);
        }
        if (!this.moveUp) {
            this.row = Math.min(this.row+1, 31);
        }

        // bounce when hit concrete or grass
        if (app.tile[this.row][this.col] != 0) {
            if (this.moveLeft && this.moveUp) {
                if (app.tile[this.row+1][this.col] != 0) {
                    this.moveLeft = false;
                }
                if (app.tile[this.row][this.col+1] != 0) {
                    this.moveUp = false;
                }
            } else if (this.moveLeft && !this.moveUp) {
                if (app.tile[this.row-1][this.col] != 0) {
                    this.moveLeft = false;
                }
                if (app.tile[this.row][this.col+1] != 0) {
                    this.moveUp = true;
                }
            } else if (!this.moveLeft && this.moveUp) {
                if (app.tile[this.row+1][this.col] != 0) {
                    this.moveLeft = true;
                }
                if (app.tile[this.row][this.col-1] != 0) {
                    this.moveUp = false;
                }
            } else if (!this.moveLeft && !this.moveUp){
                if (app.tile[this.row-1][this.col] != 0) {
                    this.moveLeft = true;
                }
                if (app.tile[this.row][this.col-1] != 0) {
                    this.moveUp = true;
                }
            }
        }

        if (!this.moveLeft) {
            this.col = col-1;
        }
        if (!this.moveUp) {
            this.row = row-1;
        }

        if (moveLeft) {
            this.x -= this.speed;
        } else {
            this.x += this.speed;
        }

        if (moveUp) {
            this.y -= this.speed;
        } else {
            this.y += this.speed;
        }

        this.updateGrid();

    }

    public static void main (String[] args) {
        // Enemy e1 = new Worm(0, 13);
        // int v[] = {0, 17};
        // int tile[][] = {{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
        //                 {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        // System.out.println(e1.isInSameRegionWithPlayer(v, tile));
        
    }
}
