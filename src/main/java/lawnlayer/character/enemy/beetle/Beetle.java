package lawnlayer.character.enemy.beetle;

import lawnlayer.App;
import lawnlayer.character.enemy.Enemy;

public class Beetle extends Enemy {
    
    /**
     * Constructor for Beetle with random position
     * @param tile Represent the type of tile of all the grids
     */
    public Beetle(int[][] tile) {
        super(tile);
    }

    /**
     * Constructor for Bettle with givin position
     * @param initialRow initial row
     * @param initialCol initial column
     */
    public Beetle(int initialRow, int initialCol) {
        super(initialRow, initialCol);
    }

    /**
     * Update the position of the <code>Beetle</code> object every frame
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
                if (app.tile[this.row+1][this.col] != 0 && app.tile[this.row][this.col+1] == 0) {
                    this.moveLeft = false;
                    if (app.tile[this.row+1][this.col] == 4) {
                        app.tile[this.row+1][this.col] = 0;
                        app.numOfGrass--;
                    }
                } else if (app.tile[this.row+1][this.col] == 0 && app.tile[this.row][this.col+1] != 0) {
                    this.moveUp = false;
                    if (app.tile[this.row][this.col+1] == 4) {
                        app.tile[this.row][this.col+1] = 0;
                        app.numOfGrass--;
                    }
                } else {
                    this.moveLeft = false;
                    this.moveUp = false;
                    if (app.tile[this.row][this.col+1] == 4) {
                        app.tile[this.row][this.col+1] = 0;
                        app.numOfGrass--;
                    }
                }
            } else if (this.moveLeft && !this.moveUp) {
                if (app.tile[this.row-1][this.col] != 0) {
                    this.moveLeft = false;
                    if (app.tile[this.row-1][this.col] == 4) {
                        app.tile[this.row-1][this.col] = 0;
                        app.numOfGrass--;
                    }
                } else if (app.tile[this.row][this.col+1] != 0) {
                    this.moveUp = true;
                    if (app.tile[this.row][this.col+1] == 4) {
                        app.tile[this.row][this.col+1] = 0;
                        app.numOfGrass--;
                    }
                } else {
                    this.moveLeft = false;
                    this.moveUp = true;
                    if (app.tile[this.row][this.col+1] == 4) {
                        app.tile[this.row][this.col+1] = 0;
                        app.numOfGrass--;
                    }
                }
            } else if (!this.moveLeft && this.moveUp) {
                if (app.tile[this.row+1][this.col] != 0) {
                    this.moveLeft = true;
                    if (app.tile[this.row+1][this.col] == 4) {
                        app.tile[this.row+1][this.col] = 0;
                        app.numOfGrass--;
                    }
                } else if (app.tile[this.row][this.col-1] != 0) {
                    this.moveUp = false;
                    if (app.tile[this.row][this.col-1] == 4) {
                        app.tile[this.row][this.col-1] = 0;
                        app.numOfGrass--;
                    }
                } else {
                    this.moveLeft = true;
                    this.moveUp = false;
                    if (app.tile[this.row][this.col+1] == 4) {
                        app.tile[this.row][this.col+1] = 0;
                        app.numOfGrass--;
                    }
                }
            } else if (!this.moveLeft && !this.moveUp){
                if (app.tile[this.row-1][this.col] != 0) {
                    this.moveLeft = true;
                    if (app.tile[this.row-1][this.col] == 4) {
                        app.tile[this.row-1][this.col] = 0;
                        app.numOfGrass--;
                    }
                } else if (app.tile[this.row][this.col-1] != 0) {
                    this.moveUp = true;
                    if (app.tile[this.row][this.col-1] == 4) {
                        app.tile[this.row][this.col-1] = 0;
                        app.numOfGrass--;
                    }
                } else {
                    this.moveLeft = true;
                    this.moveUp = true;
                    if (app.tile[this.row][this.col+1] == 4) {
                        app.tile[this.row][this.col+1] = 0;
                        app.numOfGrass--;
                    }
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
}
